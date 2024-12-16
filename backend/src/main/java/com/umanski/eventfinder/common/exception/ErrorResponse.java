package com.umanski.eventfinder.common.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Error response")
public class ErrorResponse {

    @Schema(description = "Timestamp of the error occurrence", example = "2024-05-19T08:37:11.256+00:00")
    private String timestamp;

    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "Error message", example = "Bad Request")
    private String error;

    @Schema(description = "Request path", example = "/api/v1/auth")
    private String path;

}
