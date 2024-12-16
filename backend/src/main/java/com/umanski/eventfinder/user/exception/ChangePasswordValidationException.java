package com.umanski.eventfinder.user.exception;


import com.umanski.eventfinder.common.exception.ValidationException;

public class ChangePasswordValidationException extends ValidationException {

    public ChangePasswordValidationException(String message) {
        super(message);
    }

}
