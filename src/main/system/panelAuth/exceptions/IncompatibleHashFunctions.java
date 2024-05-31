package main.system.panelAuth.exceptions;

public class IncompatibleHashFunctions extends Exception {

    public IncompatibleHashFunctions() {
    }

    public IncompatibleHashFunctions(String message) {
        super(message);
    }

    public IncompatibleHashFunctions(String message, Throwable cause) {
        super(message, cause);
    }

    public IncompatibleHashFunctions(Throwable cause) {
        super(cause);
    }

    public IncompatibleHashFunctions(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
