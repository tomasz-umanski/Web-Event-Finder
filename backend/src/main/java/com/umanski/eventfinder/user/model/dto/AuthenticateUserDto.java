package com.umanski.eventfinder.user.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Data Transfer Object for authenticating an existing user")
public class AuthenticateUserDto {

    @Email(message = "Provide a valid email address")
    @NotBlank(message = "Email field is mandatory")
    @Size(max = 50, message = "Email address must not exceed 50 characters")
    @Schema(description = "User's email address", example = "john.doe@example.com")
    private String email;

    @NotBlank(message = "Password field is mandatory")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Schema(description = "User's password", example = "strongpassword123")
    private String password;

}
