package com.umanski.eventfinder.user.exception;


import com.umanski.eventfinder.common.exception.ValidationException;

public class TokenValidationException extends ValidationException {

    public TokenValidationException(String message) {
        super(message);
    }

}
