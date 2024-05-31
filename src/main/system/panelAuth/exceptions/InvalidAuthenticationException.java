package main.system.panelAuth.exceptions;

import org.springframework.security.core.AuthenticationException;

public class InvalidAuthenticationException extends Exception {

    public InvalidAuthenticationException() {
    }

    public InvalidAuthenticationException(String message) {
        super(message);
    }

    public InvalidAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAuthenticationException(Throwable cause) {
        super(cause);
    }

    public InvalidAuthenticationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
