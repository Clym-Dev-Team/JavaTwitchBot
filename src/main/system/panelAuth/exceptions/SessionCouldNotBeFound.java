package main.system.panelAuth.exceptions;

public class SessionCouldNotBeFound extends Exception {

    public SessionCouldNotBeFound() {
    }

    public SessionCouldNotBeFound(String message) {
        super(message);
    }

    public SessionCouldNotBeFound(String message, Throwable cause) {
        super(message, cause);
    }

    public SessionCouldNotBeFound(Throwable cause) {
        super(cause);
    }

    public SessionCouldNotBeFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
