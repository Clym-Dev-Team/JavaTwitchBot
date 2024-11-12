package talium.system.templateParser.ifParser;

import talium.system.templateParser.CharakterStream;
import talium.system.templateParser.exeptions.TemplateSyntaxException;
import talium.system.templateParser.exeptions.UnexpectedEndOfInputException;
import talium.system.templateParser.exeptions.UnsupportedComparisonOperator;
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
    public static Comparison parse(String srcString) throws UnsupportedComparisonOperator {
        CharakterStream src = new CharakterStream(srcString);
        IfToken[] tokens = new IfToken[3];
        for (int i = 0; i < 3; i++) {
            tokens[i] = parseToken(src);
        }

        if (tokens[0].kind() == IfTokenKind.COMPARISON) {
            throw new TemplateSyntaxException(STR."Expected IfToken of type other than a comparison operator as the first token of a comparison!");
        }
        if (tokens[1].kind() != IfTokenKind.COMPARISON) {
            throw new TemplateSyntaxException(STR."Expected IfToken of type comparison operator as the second token of a comparison!");
        }
        if (tokens[2].kind() == IfTokenKind.COMPARISON) {
            throw new TemplateSyntaxException(STR."Expected IfToken of type other than a comparison operator as the third token of a comparison!");
        }

        Equals equals = switch (tokens[1].value()) {
            case "==" -> Equals.EQUALS;
            case "!=" -> Equals.NOT_EQUALS;
            case "<" -> Equals.LESS_THAN;
            case "<=" -> Equals.LESS_THAN_OR_EQUALS;
            case ">" -> Equals.GREATER_THAN;
            case ">=" -> Equals.GREATER_THAN_OR_EQUALS;
            default -> throw new UnsupportedComparisonOperator(STR."Not a valid Comparison Operator: \{tokens[1].value()}");
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
        if (src.isEOF()) {
            throw new UnexpectedEndOfInputException();
        }
        src.skipWhitespace();

        if (src.peek() == '"') { // hard coded Strings
            src.consume('"');
            String until = src.readUntil('"');
            src.consume('"');
            return new IfToken(IfTokenKind.STRING, until);

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
            case INT -> {
                var initialLong = Long.parseLong(token.value());
                if (initialLong > Integer.MIN_VALUE && initialLong < Integer.MAX_VALUE) {
                    yield (int) initialLong;
                }
                yield initialLong;
            }
            case DOUBLE -> Double.parseDouble(token.value());
            case BOOLEAN -> Boolean.parseBoolean(token.value());
            case COMPARISON -> throw new RuntimeException(STR."Another Comparison not a valid Object for an comparison comparand");
        };
    }
}
