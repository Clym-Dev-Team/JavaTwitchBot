package main.system.panelAuth.exceptions;

public class AuthenticationRejected extends RuntimeException{
    public AuthenticationRejected() {
    }

    public AuthenticationRejected(String message) {
        super(message);
    }

    public AuthenticationRejected(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationRejected(Throwable cause) {
        super(cause);
    }

    public AuthenticationRejected(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
