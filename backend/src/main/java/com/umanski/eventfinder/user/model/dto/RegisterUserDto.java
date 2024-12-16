package com.umanski.eventfinder.user.model.dto;

import com.umanski.eventfinder.user.validator.PasswordMatches;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Data Transfer Object for registering a new user")
@PasswordMatches(message = "Password and confirm password do not match")
public class RegisterUserDto {

    @Email(message = "Provide a valid email address")
    @NotBlank(message = "Email field is mandatory")
    @Size(max = 50, message = "Email address must not exceed 50 characters")
    @Schema(description = "User's email address", example = "john.doe@example.com")
    private String email;

    @NotBlank(message = "First name field is mandatory")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @NotBlank(message = "Last name field is mandatory")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @NotBlank(message = "Password field is mandatory")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Schema(description = "User's password", example = "strongpassword123")
    private String password;

    @NotBlank(message = "Confirm password field is mandatory")
    @Size(max = 100, message = "Confirm password must not exceed 100 characters")
    @Schema(description = "User's password confirmation", example = "strongpassword123")
    private String confirmPassword;

    public void trimFields() {
        this.email = (email != null) ? email.trim() : null;
        this.firstName = (firstName != null) ? firstName.trim() : null;
        this.lastName = (lastName != null) ? lastName.trim() : null;
        this.password = (password != null) ? password.trim() : null;
        this.confirmPassword = (confirmPassword != null) ? confirmPassword.trim() : null;
    }

}
