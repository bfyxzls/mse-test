package com.pkulaw.exception;

/**
 * @author lind
 * @date 2023/12/5 13:31
 * @since 1.0.0
 */
public class AuthException extends RuntimeException {
    private int httpStatus = 403;

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}
