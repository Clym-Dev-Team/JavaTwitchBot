package main.system.stringTemplates;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TemplateResolver {

    public static String populate(String template, HashMap<String, Object> values) {
        String output = "";
        String remaining = template;

        while (!remaining.isEmpty()) {
            int varIndex = remaining.indexOf("${");
            int directiveIndex = remaining.indexOf("%{");

            if ((varIndex < directiveIndex || directiveIndex == -1) && varIndex != -1) {
                //next is var template
                String startDefinition = remaining.substring(varIndex);
                int endBrace = startDefinition.indexOf("}");

                String varName = startDefinition.substring(2, endBrace).trim();
                String replacementValue = getNestedReplacement(varName, values).toString();

                output += remaining.substring(0, varIndex);
                output += replacementValue;
                remaining = remaining.substring(varIndex + endBrace + 1);
            } else if ((directiveIndex < varIndex || varIndex == -1) && directiveIndex != -1) {
                //next is directive template
                String startDefinition = remaining.substring(directiveIndex);
                int endBrace = startDefinition.indexOf("}");
                String definition = startDefinition.substring(2, endBrace).trim();
                String[] arguments = splitAtChar(definition, ' ');
                if (arguments.length != 0 && arguments[0].equals("for")) {
                    assert arguments.length == 4;
                    assert arguments[2].equals("in");
                    Object target = getNestedReplacement(arguments[3], values);
                    int endTagStart = startDefinition.indexOf("%{ endfor }");
                    String loop = startDefinition.substring(endBrace + 1 , endTagStart);
                    int endTagEnd = startDefinition.substring(endTagStart).indexOf("}") + 1;
                    Iterable collection;
                    try {
                        collection = (Iterable) target;
                    } catch (ClassCastException e) {
                        //TODO
                        throw new RuntimeException();
                    }

                    output += remaining.substring(0, directiveIndex);
                    for (Object item: collection) {
                        //TODO copy needed?
                        values.put(arguments[1], item);
                        output += populate(loop, values);
                    }
                    remaining = remaining.substring(directiveIndex + endTagStart + endTagEnd);
                } else if (arguments.length != 0 && arguments[0].equals("if")) {

                } else {
                    //TODO error, statement not supported
                }
            } else {
                //both Indices are -1
                break;
            }
        }
        output += remaining;
        return output;
    }

    /**
     * Tries to resolve and get the Value at the given variable path.
     * Throws an Exception if the path does not exist.
     *
     * @apiNote Does not support getters or any functions
     * @param varName dot . delimited path of variable names
     * @param values List of top level variables
     * @return value of the variable
     */
    private static Object getNestedReplacement(String varName, HashMap<String, Object> values) {
        String[] variableNames = splitAtChar(varName, '.');
        Object variable = values.get(variableNames[0]);
        for (int i = 1; i < variableNames.length; i++) {
            try {
                Field declaredField = variable.getClass().getDeclaredField(variableNames[i]);
                declaredField.setAccessible(true);
                variable = declaredField.get(variable);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            } //TODO better error handling, custom Exception with Text, logging
        }
        return variable;
    }

    /**
     * Splits are String at a given Char.
     * Does not compute a regex.
     * @apiNote Does not support String escaping!
     * @param input string to be split
     * @param splitChar delimiter char
     * @return the Parts delimited by the splitChar, the splitChar is no longer contained in the Array
     */
    private static String[] splitAtChar(String input, char splitChar) {
        ArrayList<String> parts = new ArrayList<>();
        String remaining = input;
        int index = remaining.indexOf(splitChar);
        while (index != -1) {
            parts.add(remaining.substring(0, index));
            remaining = remaining.substring(index + 1);
            index = remaining.indexOf(splitChar);
        }
        parts.add(remaining);
        return parts.toArray(new String[0]);
    }

}
