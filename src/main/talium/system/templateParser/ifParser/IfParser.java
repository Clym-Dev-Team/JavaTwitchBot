package talium.system.templateParser.ifParser;

import talium.system.templateParser.CharakterStream;
import talium.system.templateParser.UnexpectedEndOfInputException;
import talium.system.templateParser.statements.Equals;
import talium.system.templateParser.statements.VarStatement;
import talium.system.templateParser.tokens.Comparison;
import talium.system.templateParser.tokens.IfTokenKind;
import talium.system.templateParser.tokens.IfToken;

/**
 * Takes a String of an If Statements Head, and parses it into an If Statement Object.
 * Allowed comparison operators are: <br>
 * - == <br>
 * - != <br>
 * - < <br>
 * - <= <br>
 * - > <br>
 * - >= <br>
 *
 * @apiNote Logical Operators (&& - AND, || - OR) and parentheses are not supported yet.
 */
public class IfParser {
    //TODO logical operators, parentheses

    /**
     * Parses the entire If head into a Comparison that can be evaluated
     * @param srcString entire unparsed if head
     * @return Comparison object
     */
    public static Comparison parse(String srcString) {
        CharakterStream src = new CharakterStream(srcString);
        IfToken[] tokens = new IfToken[3];
        for (int i = 0; i < 3; i++) {
            tokens[i] = parseToken(src);
        }
        assert tokens[0].kind() != IfTokenKind.COMPARISON;
        assert tokens[1].kind() == IfTokenKind.COMPARISON;
        assert tokens[2].kind() != IfTokenKind.COMPARISON;

        Equals equals = switch (tokens[1].value()) {
            case "==" -> Equals.EQUALS;
            case "!=" -> Equals.NOT_EQUALS;
            case "<" -> Equals.LESS_THAN;
            case "<=" -> Equals.LESS_THAN_OR_EQUALS;
            case ">" -> Equals.GREATER_THAN;
            case ">=" -> Equals.GREATER_THAN_OR_EQUALS;
            default -> throw new IllegalStateException(STR."Not a valid Comparison Operator: \{tokens[1].value()}");
        };
        return new Comparison(tokenToObject(tokens[0]), equals, tokenToObject(tokens[2]));
    }

    /**
     * detects, parses and then consumes the entire next token
     *
     * @param src source character stream
     * @return the parsed token
     */
    private static IfToken parseToken(CharakterStream src) throws UnexpectedEndOfInputException {
        //TODO rebuild to consume one character per loop, save current state in var
        //TODO make return optional
        if (src.isEOF()) {
            throw new UnexpectedEndOfInputException();
        }
//        System.out.println(src.src.substring(src.pos));
        src.skipWhitespace();

        if (src.peek() == '"') { // hard coded Strings
            src.next(); // consume first "
            return new IfToken(IfTokenKind.STRING, src.readUntil('"'));

        } else if (Character.isDigit(src.peek())) { // numbers
            String number = src.readTillWhitespace();
            if (number.indexOf('.') >= 0) {
                return new IfToken(IfTokenKind.DOUBLE, number);
            }
            return new IfToken(IfTokenKind.INT, number);

        } else if ("!=<>".indexOf(src.peek()) >= 0) { // comparison operator
            return new IfToken(IfTokenKind.COMPARISON, src.readTillWhitespace());
        }

        // booleans
        String remainingTillSpace = src.readTillWhitespace();
        if (remainingTillSpace.equalsIgnoreCase("true")) {
            return new IfToken(IfTokenKind.BOOLEAN, "true");
        } else if (remainingTillSpace.equalsIgnoreCase("false")) {
            return new IfToken(IfTokenKind.BOOLEAN, "false");
        }

        // reference to variable
        return new IfToken(IfTokenKind.VAR, remainingTillSpace);
    }

    /**
     * get inner value out of token
     */
    private static Object tokenToObject(IfToken token) {
        return switch (token.kind()) {
            case STRING -> token.value();
            case VAR -> new VarStatement(token.value());
            case INT -> Integer.parseInt(token.value());
            case DOUBLE -> Double.parseDouble(token.value());
            case BOOLEAN -> Boolean.parseBoolean(token.value());
            case COMPARISON -> throw new RuntimeException(STR."Another Comparison not a valid Object for an comparison comparand");
        };
    }
}
