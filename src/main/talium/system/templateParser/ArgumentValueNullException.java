package talium.system.templateParser;

public class ArgumentValueNullException extends Exception {
    public ArgumentValueNullException(String argName) {
        super(STR."\{argName == null ? "Argument" : argName} must not be null.");
    }
}
