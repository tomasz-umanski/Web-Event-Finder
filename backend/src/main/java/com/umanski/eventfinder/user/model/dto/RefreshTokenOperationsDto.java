package com.umanski.eventfinder.user.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Data Transfer Object for refresh token operations")
public class RefreshTokenOperationsDto {

    @NotBlank(message = "Refresh token field is mandatory")
    @Size(max = 512, message = "Refresh token must not exceed 512 characters")
    @Schema(description = "Refresh token used for authentication", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;

}
