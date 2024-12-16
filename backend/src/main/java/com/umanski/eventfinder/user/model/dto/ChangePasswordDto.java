package com.umanski.eventfinder.user.model.dto;

import com.umanski.eventfinder.user.validator.ChangedPasswordMatches;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "Data Transfer Object for changing password of an existing user")
@ChangedPasswordMatches()
public class ChangePasswordDto {

    @NotBlank(message = "Current password field is mandatory")
    @Size(min = 8, max = 100, message = "Current password must be between 8 and 100 characters")
    @Schema(description = "User's current password", example = "strongpassword123")
    private String currentPassword;

    @NotBlank(message = "New password field is mandatory")
    @Size(min = 8, max = 100, message = "New password must be between 8 and 100 characters")
    @Schema(description = "User's new password", example = "strongpassword123")
    private String newPassword;

    @NotBlank(message = "Confirm new password field is mandatory")
    @Size(max = 100, message = "Confirm password must not exceed 100 characters")
    @Schema(description = "User's new password confirmation", example = "strongpassword123")
    private String confirmNewPassword;

}
