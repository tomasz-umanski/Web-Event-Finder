package com.umanski.eventfinder.user;

import com.umanski.eventfinder.user.exception.AuthenticationValidationException;
import com.umanski.eventfinder.user.exception.RegisterValidationException;
import com.umanski.eventfinder.user.exception.TokenValidationException;
import com.umanski.eventfinder.user.exception.UserSaveException;
import com.umanski.eventfinder.user.model.dto.AuthenticateUserDto;
import com.umanski.eventfinder.user.model.dto.AuthenticationResponse;
import com.umanski.eventfinder.user.model.dto.RefreshTokenOperationsDto;
import com.umanski.eventfinder.user.model.dto.RegisterUserDto;
import com.umanski.eventfinder.user.model.entity.User;
import com.umanski.eventfinder.user.model.enums.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthenticationResponse register(RegisterUserDto registerUserDto) {
        validateRegistrationRequest(registerUserDto);
        User newUser = createUserFromDto(registerUserDto);
        User savedUser = persistUser(newUser);
        return generateAuthenticationTokens(savedUser);
    }

    @Override
    @Transactional
    public AuthenticationResponse authenticate(AuthenticateUserDto authenticateUserDto) {
        authenticateUserCredentials(authenticateUserDto);
        User user = retrieveUserByEmail(authenticateUserDto.getEmail());
        return generateAuthenticationTokens(user);
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenOperationsDto refreshTokenOperationsDto) {
        User user = validateAndExtractUserFromRefreshToken(refreshTokenOperationsDto.getRefreshToken());
        return generateAuthenticationTokens(user);
    }

    @Override
    public void logout(RefreshTokenOperationsDto refreshTokenOperationsDto) {
        validateAndExtractUserFromRefreshToken(refreshTokenOperationsDto.getRefreshToken());
        performLogout(refreshTokenOperationsDto.getRefreshToken());
    }

    private void validateRegistrationRequest(RegisterUserDto registerUserDto) {
        registerUserDto.trimFields();
        if (userRepository.existsByEmailIgnoreCase(registerUserDto.getEmail())) {
            throw new RegisterValidationException("User with this email already exists");
        }
    }

    private User createUserFromDto(RegisterUserDto registerUserDto) {
        User newUser = UserMapper.INSTANCE.toUser(registerUserDto);
        String hashedPassword = passwordEncoder.encode(registerUserDto.getPassword());
        newUser.setPassword(hashedPassword);
        newUser.setRole(Role.USER);
        return newUser;
    }

    private User persistUser(User user) {
        try {
            User savedUser = userRepository.save(user);
            log.info("Saved new user in repository with id = {}", savedUser.getId());
            return savedUser;
        } catch (Exception e) {
            log.error("Failed to save new user in repository", e);
            throw new UserSaveException("Failed to create new user", e);
        }
    }

    private void authenticateUserCredentials(AuthenticateUserDto authenticateUserDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticateUserDto.getEmail(),
                            authenticateUserDto.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new AuthenticationValidationException("Invalid credentials", e);
        }
    }

    private User retrieveUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new AuthenticationValidationException("User not found"));
    }

    private User validateAndExtractUserFromRefreshToken(String refreshToken) {
        User user = (User) jwtService.extractUser(refreshToken);
        if (!jwtService.isRefreshTokenValid(refreshToken, user)) {
            throw new TokenValidationException("Invalid refresh token");
        }
        return user;
    }

    private AuthenticationResponse generateAuthenticationTokens(User user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new AuthenticationResponse(accessToken, refreshToken);
    }

    private void performLogout(String refreshToken) {
        jwtService.revokeToken(refreshToken);
        SecurityContextHolder.clearContext();
    }

}
