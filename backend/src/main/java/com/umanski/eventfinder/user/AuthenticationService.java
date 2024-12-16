package com.umanski.eventfinder.user;

import com.umanski.eventfinder.user.model.dto.AuthenticateUserDto;
import com.umanski.eventfinder.user.model.dto.AuthenticationResponse;
import com.umanski.eventfinder.user.model.dto.RefreshTokenOperationsDto;
import com.umanski.eventfinder.user.model.dto.RegisterUserDto;

public interface AuthenticationService {

    AuthenticationResponse register(RegisterUserDto registerUserDto);

    AuthenticationResponse authenticate(AuthenticateUserDto authenticateUserDto);

    AuthenticationResponse refreshToken(RefreshTokenOperationsDto refreshTokenOperationsDto);

    void logout(RefreshTokenOperationsDto refreshTokenOperationsDto);

}
