package com.umanski.eventfinder.user.exception;

import com.umanski.eventfinder.common.exception.SaveException;

public class UserSaveException extends SaveException {

    public UserSaveException(String message, Throwable cause) {
        super(message, cause);
    }

}
