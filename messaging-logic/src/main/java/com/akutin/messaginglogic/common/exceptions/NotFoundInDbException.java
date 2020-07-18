package com.akutin.messaginglogic.common.exceptions;

public class NotFoundInDbException extends Exception {
    public NotFoundInDbException() {
    }

    public NotFoundInDbException(String message) {
        super(message);
    }

    public NotFoundInDbException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundInDbException(Throwable cause) {
        super(cause);
    }

    public NotFoundInDbException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
