package com.umanski.eventfinder.user;

import com.umanski.eventfinder.user.model.entity.Token;
import com.umanski.eventfinder.user.model.entity.User;
import com.umanski.eventfinder.user.model.enums.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
class JwtServiceImpl implements JwtService {

    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.access-token.expiration}")
    private long jwtAccessTokenExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long jwtRefreshTokenExpiration;

    private final TokenRepository tokenRepository;
    private final UserService userService;

    @Override
    public UserDetails extractUser(String token) {
        try {
            String username = extractUsername(token);
            return userService.findUserByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        } catch (Exception e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    @Override
    public String generateAccessToken(User user) {
        Date expirationDate = new Date(System.currentTimeMillis() + jwtAccessTokenExpiration);
        return buildToken(user.getUsername(), ACCESS_TOKEN, expirationDate);
    }

    @Override
    public String generateRefreshToken(User user) {
        revokeAllUserTokens(user);
        Date expirationDate = new Date(System.currentTimeMillis() + jwtRefreshTokenExpiration);
        String token = buildToken(user.getUsername(), REFRESH_TOKEN, expirationDate);
        saveUserToken(user, token, expirationDate);
        return token;
    }

    @Override
    public Optional<String> extractAndValidateAccessToken(HttpServletRequest request) {
        return extractAndValidateToken(request, this::isAccessToken);
    }

    @Override
    public boolean isRefreshTokenValid(String refreshToken, UserDetails userDetails) {
        return isRefreshToken(refreshToken) && isTokenValid(refreshToken, userDetails);
    }

    @Override
    public void authenticateUser(String jwt, HttpServletRequest request) {
        UserDetails userDetails = extractUser(jwt);
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            setAuthenticationContext(userDetails, request);
        }
    }

    @Override
    public void revokeToken(String token) {
        tokenRepository.findByToken(token).ifPresent(storedToken -> {
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
        });
    }

    private void setAuthenticationContext(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private boolean isTokenExpired(String token) {
        return tokenRepository.findByToken(token)
                .map(storedToken -> extractExpiration(token).before(new Date()) || storedToken.isRevoked())
                .orElseGet(() -> extractExpiration(token).before(new Date()));
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        validUserTokens.forEach(token -> token.setRevoked(true));
        tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(User user, String jwtToken, Date expirationDate) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .expirationDate(expirationDate.toInstant())
                .build();
        tokenRepository.save(token);
    }

    private String buildToken(String username, String type, Date expirationDate) {
        return Jwts.builder()
                .setClaims(createClaims(type))
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Map<String, Object> createClaims(String type) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", type);
        claims.put("jti", UUID.randomUUID().toString());
        return claims;
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Optional<String> extractAndValidateToken(HttpServletRequest request, Predicate<String> tokenTypeChecker) {
        try {
            return Optional.ofNullable(extractJwtFromHeader(request))
                    .filter(tokenTypeChecker)
                    .flatMap(jwt -> {
                        UserDetails userDetails = extractUser(jwt);
                        return isTokenValid(jwt, userDetails) ? Optional.of(jwt) : Optional.empty();
                    });
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private String extractJwtFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return (authHeader != null && authHeader.startsWith("Bearer")) ? authHeader.substring(7) : null;
    }

    private boolean isAccessToken(String token) {
        return ACCESS_TOKEN.equals(extractClaim(token, claims -> claims.get("type")));
    }

    private boolean isRefreshToken(String token) {
        return REFRESH_TOKEN.equals(extractClaim(token, claims -> claims.get("type")));
    }

    private boolean isTokenValid(String token, UserDetails userDetails) {
        return !isTokenExpired(token) && extractUsername(token).equals(userDetails.getUsername());
    }

}
