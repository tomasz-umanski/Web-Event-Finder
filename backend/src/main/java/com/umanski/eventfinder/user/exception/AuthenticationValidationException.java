package com.umanski.eventfinder.user.exception;

import com.umanski.eventfinder.common.exception.ValidationException;

public class AuthenticationValidationException extends ValidationException {

    public AuthenticationValidationException(String message) {
        super(message);
    }

    public AuthenticationValidationException(String message, Throwable cause) {
        super(message, cause);
    }

}
