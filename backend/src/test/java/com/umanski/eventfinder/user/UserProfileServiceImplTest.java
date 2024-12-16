package com.umanski.eventfinder.user;

import com.umanski.eventfinder.user.exception.ChangePasswordValidationException;
import com.umanski.eventfinder.user.model.dto.ChangePasswordDto;
import com.umanski.eventfinder.user.model.entity.User;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("UnitTest")
@ExtendWith(MockitoExtension.class)
class UserProfileServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserProfileServiceImpl userProfileService;

    @Test
    void changePassword_Success() {
        User user = mockUser();
        ChangePasswordDto changePasswordDto = mockChangePasswordDto();

        when(passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(changePasswordDto.getNewPassword())).thenReturn("newEncodedPassword");

        userProfileService.changePassword(user, changePasswordDto);

        verify(passwordEncoder).encode(changePasswordDto.getNewPassword());
        verify(userRepository).save(user);

        assertEquals("newEncodedPassword", user.getPassword());
    }

    @Test
    void changePassword_InvalidCurrentPassword_ThrowsInvalidPasswordException() {
        User user = mockUser();
        ChangePasswordDto changePasswordDto = mockChangePasswordDto();

        when(passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())).thenReturn(false);

        ChangePasswordValidationException exception = assertThrows(
                ChangePasswordValidationException.class,
                () -> userProfileService.changePassword(user, changePasswordDto)
        );

        assertEquals("Current password is incorrect", exception.getMessage());
    }

    @Test
    void changePassword_NewPasswordSameAsCurrent_ThrowsInvalidPasswordException() {
        User user = mockUser();
        ChangePasswordDto changePasswordDto = mockChangePasswordDto();

        when(passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())).thenReturn(true);
        when(passwordEncoder.matches(changePasswordDto.getNewPassword(), user.getPassword())).thenReturn(true);

        ChangePasswordValidationException exception = assertThrows(
                ChangePasswordValidationException.class,
                () -> userProfileService.changePassword(user, changePasswordDto)
        );

        assertEquals("New password cannot be the same as the old password", exception.getMessage());
    }

    private User mockUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .email("john.doe@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .build();
    }

    private ChangePasswordDto mockChangePasswordDto() {
        return ChangePasswordDto.builder()
                .currentPassword("password")
                .newPassword("newPassword")
                .confirmNewPassword("newPassword")
                .build();
    }

}