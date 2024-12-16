package com.umanski.eventfinder.user.exception;


import com.umanski.eventfinder.common.exception.ValidationException;

public class RegisterValidationException extends ValidationException {

    public RegisterValidationException(String message) {
        super(message);
    }

}
