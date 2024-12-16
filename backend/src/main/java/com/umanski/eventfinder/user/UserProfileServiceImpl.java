package com.umanski.eventfinder.user;

import com.umanski.eventfinder.user.exception.ChangePasswordValidationException;
import com.umanski.eventfinder.user.exception.UserSaveException;
import com.umanski.eventfinder.user.model.dto.ChangePasswordDto;
import com.umanski.eventfinder.user.model.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void changePassword(User user, ChangePasswordDto changePasswordDto) {
        validatePasswordChange(user, changePasswordDto);
        updatePassword(user, changePasswordDto.getNewPassword());
        persistUserPassword(user);
    }

    private void validatePasswordChange(User user, ChangePasswordDto changePasswordDto) {
        validateCurrentPassword(user, changePasswordDto.getCurrentPassword());
        validateNewPassword(user, changePasswordDto.getNewPassword());
    }

    private void validateCurrentPassword(User user, String currentPassword) {
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new ChangePasswordValidationException("Current password is incorrect");
        }
    }

    private void validateNewPassword(User user, String newPassword) {
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new ChangePasswordValidationException("New password cannot be the same as the old password");
        }
    }

    private void updatePassword(User user, String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
    }

    private void persistUserPassword(User user) {
        try {
            userRepository.save(user);
            log.info("Changed password for user with id: {}", user.getId());
        } catch (Exception e) {
            log.error("Failed to change password for user with id: {}", user.getId(), e);
            throw new UserSaveException("Failed to change password", e);
        }
    }

}
