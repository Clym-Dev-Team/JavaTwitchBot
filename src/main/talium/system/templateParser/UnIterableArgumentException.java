package talium.system.templateParser;

public class UnIterableArgumentException extends Exception {
    public UnIterableArgumentException(String argName) {
        super(STR."\{argName == null ? "Argument" : argName} must implement Iterable.");
    }
}
