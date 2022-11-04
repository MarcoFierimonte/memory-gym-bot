package com.f90.telegram.bot.memorygymbot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalException extends RuntimeException {

    private static final long serialVersionUID = 3566961119668884081L;

    /**
     * Instantiates a new Internal exception.
     */
    public InternalException() {
    }

    /**
     * Instantiates a new Internal exception.
     *
     * @param message the message
     */
    public InternalException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Internal exception.
     *
     * @param message   the message
     * @param throwable the throwable
     */
    public InternalException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
