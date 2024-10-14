package talium.system.templateParser;

/**
 * Comparison is not supported between two types for a specific operator
 */
public class UnsupportedComparandType extends Exception {
    public UnsupportedComparandType(String s) {
        super(s);
    }
}