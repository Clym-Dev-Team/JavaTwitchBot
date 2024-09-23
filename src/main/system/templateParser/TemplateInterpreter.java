package main.system.templateParser;

import main.system.templateParser.statements.*;
import main.system.templateParser.tokens.Comparison;
import org.apache.commons.lang.NullArgumentException;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Populates variables and interprets parsed String templates
 */
public class TemplateInterpreter {

    /**
     * Populates variables and interprets parsed String templates
     * @param template parsed template as list of statements
     * @param values a map with all top level variables and their names
     * @return the resulting string
     */
    public static String populate(List<Statement> template, HashMap<String, Object> values) {
        try { //TODO push errors up the chain
            String out = "";
            for (Statement statement : template) {
                if (statement instanceof TextStatement textStatement) {
                    out += textStatement.text();
                } else if (statement instanceof VarStatement varStatement) {
                    out += getNestedReplacement(varStatement.name(), values).toString();
                } else if (statement instanceof IfStatement ifStatement) {
                    // replace Vars with actual values
                    Object left = ifStatement.comparison().left();
                    if (left instanceof VarStatement leftVar) {
                        left = getNestedReplacement(leftVar.name(), values).toString();
                    }
                    Object right = ifStatement.comparison().right();
                    if (right instanceof VarStatement rightVar) {
                        right = getNestedReplacement(rightVar.name(), values).toString();
                    }

                    boolean condition = IfInterpreter.compare(new Comparison(left, ifStatement.comparison().equals(), right));
                    if (condition) {
                        out += populate(ifStatement.then(), values);
                    } else {
                        out += populate(ifStatement.other(), values);
                    }
                } else if (statement instanceof LoopStatement loop) {
                    String[] varParts = loop.var().split("\\[*]");
                    Collection<Object> iterable = (Collection<Object>) getNestedReplacement(varParts[0], values); //TODO try, throw if object in loop is not iterable
                    for (Object item : iterable) {
                        values.put(loop.name(), item);
                        if (varParts.length > 1) {
                            values.put(loop.name(), getNestedReplacement(varParts[1], values));
                        }
                        out += populate(loop.body(), values);
                    }
                    values.remove(loop.name());
                }
            }
            return out;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * Tries to resolve and get the Value at the given variable path.
     * Throws an Exception if the path does not exist.
     *
     * @param varName dot . delimited path of variable names
     * @param values  List of top level variables
     * @return value of the variable
     * @apiNote Does not support getters or any functions
     */
    public static Object getNestedReplacement(String varName, HashMap<String, Object> values) throws ArgumentValueNullException, NoSuchFieldException, IllegalAccessException {
        String[] variableNames = varName.split("\\.");
        Object variable = values.get(variableNames[0]);
        for (int i = 1; i < variableNames.length; i++) {
                if (variable == null) {
                    throw new ArgumentValueNullException(variableNames[i-1]);
                }
                Field declaredField = variable.getClass().getDeclaredField(variableNames[i]);
                declaredField.setAccessible(true);
                variable = declaredField.get(variable);
        }
        return variable;
    }
}


class ArgumentValueNullException extends IllegalArgumentException {
    public ArgumentValueNullException(String argName) { //TODO make this a non runtime exception
        super(STR."\{argName == null ? "Argument" : argName} must not be null.");
    }
}