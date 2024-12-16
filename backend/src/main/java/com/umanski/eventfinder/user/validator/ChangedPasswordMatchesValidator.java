package com.umanski.eventfinder.user.validator;

import com.umanski.eventfinder.user.model.dto.ChangePasswordDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ChangedPasswordMatchesValidator implements ConstraintValidator<ChangedPasswordMatches, ChangePasswordDto> {

    @Override
    public boolean isValid(ChangePasswordDto dto, ConstraintValidatorContext context) {
        if (dto.getNewPassword() == null || dto.getConfirmNewPassword() == null) {
            return false;
        }
        return dto.getNewPassword().equals(dto.getConfirmNewPassword());
    }

}
