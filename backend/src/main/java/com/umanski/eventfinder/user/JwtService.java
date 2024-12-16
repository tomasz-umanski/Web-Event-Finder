package com.umanski.eventfinder.user;

import com.umanski.eventfinder.user.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface JwtService {

    UserDetails extractUser(String token);

    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    Optional<String> extractAndValidateAccessToken(HttpServletRequest request);

    boolean isRefreshTokenValid(String refreshToken, UserDetails userDetails);

    void authenticateUser(String jwt, HttpServletRequest request);

    void revokeToken(String token);

}
