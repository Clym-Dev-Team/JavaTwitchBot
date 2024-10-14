package talium.system.templateParser;

/**
 * Comparison Operator (==, >, >=, ...) not supported
 */
public class UnsupportedComparisonOperator extends Exception {
    public UnsupportedComparisonOperator(String s) {
        super(s);
    }
}
