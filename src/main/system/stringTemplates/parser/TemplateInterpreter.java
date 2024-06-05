package main.system.stringTemplates.parser;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.expression.spel.ast.Assign;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class TemplateInterpreter {

    public static String populate(List<Statement> template, HashMap<String, Object> values) {
        String out = "";
        for (Statement statement : template) {
            switch (statement) {
                case TextStatement t -> out += t.text();
                case VarStatement v -> out += varResolver(v.name(), values).toString();
                case IfStatement i -> {
                    var con = i.condition();
                    String left = con.isLeftVar() ? varResolver(con.left(), values).toString() : con.left();
                    String right = con.isRightVar() ? varResolver(con.right(), values).toString() : con.right();
                    if (compare(left, con.equals(), right)) {
                        out += populate(i.then(), values);
                    } else {
                        out += populate(i.other(), values);
                    }
                }
                case LoopStatement l -> {
                    var parts = l.var().split("\\[\\*]");
                    assert parts.length <= 2;
                    Collection coll = (Collection) varResolver(parts[0], values); //TODO try
                    for (var v: coll) {
                        values.put(l.name(), v);
                        if (parts.length > 1) {
                            values.put(l.name(), varResolver(l.name() + parts[1], values));
                        }
                        out += populate(l.body(), values);
                    }
                    values.remove(l.name());
                }
                default -> {
                    //TODO error
                }
            }
        }
        return out;
    }

    static Object varResolver(String varName, HashMap<String, Object> values) {
        String[] variableNames = varName.split("\\.");
        try {
            if (!values.containsKey(variableNames[0])) {
                System.out.println("Variable " + ArrayUtils.toString(variableNames) + " not found");
                System.out.println("in: " + varName);
                //TODO error
            }
            Object variable = values.get(variableNames[0]);
            for (int i = 1; i < variableNames.length; i++) {
                Field declaredField = variable.getClass().getDeclaredField(variableNames[i]);
                declaredField.setAccessible(true);
                variable = declaredField.get(variable);
            }
            return variable;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } //TODO better error handling, custom Exception with Text, logging
    }

    static boolean compare(String left, Equals comparison, String right) {
        return switch (comparison) {
            case EQUALS -> left.equals(right);
            case NOT_EQUALS -> !left.equals(right);
            case GREATER_THAN -> throw new RuntimeException("Currently only EQUALS and NOT_EQUALS are supported");
            case GREATER_THAN_OR_EQUALS ->
                    throw new RuntimeException("Currently only EQUALS and NOT_EQUALS are supported");
            case LESS_THAN -> throw new RuntimeException("Currently only EQUALS and NOT_EQUALS are supported");
            case LESS_THAN_OR_EQUALS ->
                    throw new RuntimeException("Currently only EQUALS and NOT_EQUALS are supported");
        };
    }
}
