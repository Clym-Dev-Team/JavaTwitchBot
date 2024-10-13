package talium.system.stringTemplates.parser;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class UnsupportedTokenException extends RuntimeException {
    public UnsupportedTokenException() {
    }

    public UnsupportedTokenException(String message) {
        super(message);
    }

    public UnsupportedTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedTokenException(Throwable cause) {
        super(cause);
    }

    public UnsupportedTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

interface Statement {}

record TextStatement(String text) implements Statement {}

record VarStatement(String name) implements Statement {}

record LoopStatement(String name, String var, List<Statement> body) implements Statement {}

record IfStatement(Condition condition, List<Statement> then, List<Statement> other) implements Statement {}

record Condition(String left, boolean isLeftVar, Equals equals, String right, boolean isRightVar) implements Statement {}

enum Equals {
    EQUALS,
    NOT_EQUALS,
    GREATER_THAN,
    GREATER_THAN_OR_EQUALS,
    LESS_THAN,
    LESS_THAN_OR_EQUALS,
}

public class TemplateParser {
    private static final Pattern conditionRegex = Pattern.compile("(.*) ([!=<>]{1,2}) (.*)");
    TemplateTokeniser src;

    public TemplateParser(String template) {
        this.src = new TemplateTokeniser(template);
    }

    public List<Statement> parse() {
        List<Statement> statements = new ArrayList<>();
        while (!src.isEOF()) {
            statements.add(parseToken());
        }
        return statements;
    }

    Statement parseToken() {
        var current = src.next();
        if (current == null) {
            return null;
        }
        if (current.getClass() == TextToken.class) {
            return new TextStatement(((TextToken) current).text());
        }
        if (current.getClass() == VarToken.class) {
            return new VarStatement(((VarToken) current).varName());
        }
        if (current.getClass() == IfConditionToken.class) {
            String condition = ((IfConditionToken) current).condition();
            List<Statement> then = new ArrayList<>();
            List<Statement> other = new ArrayList<>();
            boolean isElse = false;
            while (!src.isEOF()) { // mostly equivalent to while(true), but with a overrun check
                var curr = src.peek();
                if (curr.getClass() == IfElseToken.class) {
                    isElse = true;
                    src.next(); //consume else Token
                    continue;
                }
                if (curr.getClass() == IfEndToken.class) {
                    src.next(); //consume ifEnd Token
                    break;
                }
                if (!isElse) {
                    then.add(parseToken());
                } else {
                    other.add(parseToken());
                }
            }
            return new IfStatement(parseCondition(condition), then, other);
        }
        if (current.getClass() == ForHeadToken.class) {
            String name = ((ForHeadToken) current).name();
            String var = ((ForHeadToken) current).var();
            if (StringUtils.countMatches(var, "[*]") > 1) {
                throw new RuntimeException("Only one wildcard allowed in a for statement");
            }
            List<Statement> body = new ArrayList<>();
            while (!src.isEOF()) { // mostly equivalent to while(true), but with a overrun check
                var curr = src.peek();
                if (curr.getClass() == ForEndTokenToken.class) {
                    src.next();
                    break;
                }
                body.add(parseToken());
            }
            return new LoopStatement(name, var, body);

        }
        return null;
    }

    private static Condition parseCondition(String condition) {
        String trimmed = condition.trim();
        Matcher matcher = conditionRegex.matcher(trimmed);
        if (!matcher.find()) {
            System.err.println(STR."Condition: \{condition} could not be evaluated!");
            System.err.println("NOTE: Whitespace between equals and sides of Condition is mandatory!");
            throw new IllegalArgumentException();
        }
        String left = matcher.group(1).trim();
        boolean leftIsLiteral = left.startsWith("\"") && left.endsWith("\"");
        if (leftIsLiteral) {
            left = left.substring(1, left.length() - 1);
        }
        String eqString = matcher.group(2).trim();
        String right = matcher.group(3).trim();
        boolean rightIsLiteral = right.startsWith("\"") && right.endsWith("\"");
        if (rightIsLiteral) {
            right = right.substring(1, right.length() - 1);
        }

        Equals equals = switch (eqString) {
            case "==" -> Equals.EQUALS;
            case "!=" -> Equals.NOT_EQUALS;
            case "<" -> Equals.LESS_THAN;
            case "<=" -> Equals.LESS_THAN_OR_EQUALS;
            case ">" -> Equals.GREATER_THAN;
            case ">=" -> Equals.GREATER_THAN_OR_EQUALS;
            default -> throw new IllegalStateException(STR."Not a valid Comparison Operator: \{eqString}");
        };
        return new Condition(left, !leftIsLiteral, equals, right, !rightIsLiteral);

    }

    public static void debugPrint(List<Statement> statements, int depth) {
        String indent = " ".repeat(depth);
        for (var statement : statements) {
            if (statement instanceof TextStatement textStatement) {
                System.out.println(STR."\{indent}Text(\{textStatement.text()}),");
            }
            if (statement instanceof VarStatement varStatement) {
                System.out.println(STR."\{indent}Var(\{varStatement.name()}),");
            }
            if (statement instanceof IfStatement ifStatement) {
                System.out.println(STR."\{indent}IF: \{ifStatement.condition()}");
                debugPrint(ifStatement.then(),depth+1);
                System.out.println(STR."\{indent}} else {");
                debugPrint(ifStatement.other(),depth+1);
                System.out.println(STR."\{indent}},");
            }
            if (statement instanceof LoopStatement forStatement) {
                System.out.println(STR."\{indent}FOR: \{forStatement.name()} in \{forStatement.var()} {");
                debugPrint(forStatement.body(),depth+1);
                System.out.println(STR."\{indent}},");
            }
        }
    }
}
