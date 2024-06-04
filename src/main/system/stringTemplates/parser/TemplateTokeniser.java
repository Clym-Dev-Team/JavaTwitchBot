package main.system.stringTemplates.parser;

import java.util.ArrayList;
import java.util.List;


class UnexpectedToken extends RuntimeException {
    public UnexpectedToken() {
    }

    public UnexpectedToken(String message) {
        super(message);
    }

    public UnexpectedToken(String message, Throwable cause) {
        super(message, cause);
    }

    public UnexpectedToken(Throwable cause) {
        super(cause);
    }

    public UnexpectedToken(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

interface Token {}

record TextToken(String text) implements Token {}

record VarToken(String varName) implements Token {}

record IfConditionToken(String condition) implements Token {}

record IfElseToken() implements Token {}

record IfEndToken() implements Token {}

record ForHeadToken(String name, String var) implements Token {}

record ForEndTokenToken() implements Token {}

public class TemplateTokeniser implements TokenStream<Token> {
    CharakterStream src;
    List<Token> tokens;
    int pos;

    public TemplateTokeniser(String src) {
        this.src = new CharakterStream(src);
        this.tokens = new ArrayList<>();
    }

    public List<Token> parse() {
        List<Token> list = new ArrayList<>();
        while (!src.isEOF()) {
            Token next = next();
            if (next == null) {
                break;
            }
            list.add(next);
        }
        return list;
    }

    @Override
    public Token peek() {
        if (pos >= tokens.size()) {
            tokens.add(parseToken());
        }
        return tokens.get(pos);
    }

    @Override
    public Token next() {
        if (pos >= tokens.size()) {
            tokens.add(parseToken());
        }
        Token t = tokens.get(pos);
        pos += 1;
        return t;
    }

    private Token parseToken() {
        if (src.isEOF()) {
            return null;
        }
        if (src.peek() == '$' && src.future() == '{') {
            src.next(); // consume $
            src.next(); // consume {
            String varName = readTillClosing();
            src.next(); //consume }
            return new VarToken(varName);

        } else if (src.peek() == '%' && src.future() == '{') {
            // Get type of directive if or for
            src.next(); //consume %
            src.next(); //consume {
            String directive = readTillWhitespace();
            skipWhitespace();

            if (directive.equals("if")) {
                String condition = readTillClosing();
                skipWhitespace();
                src.next(); //consume }
                return new IfConditionToken(condition);

            } else if (directive.equals("else")) {
                skipWhitespace();
                src.next(); //consume }
                return new IfElseToken();

            } else if (directive.equals("endif")) {
                skipWhitespace();
                src.next(); //consume }
                return new IfEndToken();

            } else if (directive.equals("for")) {
                String name = readTillWhitespace();
                skipWhitespace();
                src.next(); //consume i
                src.next(); //consume n
                skipWhitespace();
                String var = readTillClosing();
                skipWhitespace();
                src.next(); //consume }
                return new ForHeadToken(name, var);

            } else if (directive.equals("endfor")) {
                skipWhitespace();
                src.next(); //consume }
                return new ForEndTokenToken();

            } else {
                throw new UnexpectedToken(STR."Directive not supported: \{directive}");
            }
        } else {
            String buffer = "";
            while (!src.isEOF() && src.peek() != '$' && src.peek() != '%') {
                buffer += src.next();
            }
            return new TextToken(buffer);
        }
    }

    private String readTillClosing() {
        skipWhitespace();
        String buffer = "";
        while (src.peek() != '}') {
            buffer += src.next();
        }
        return buffer;
    }

    private String readTillWhitespace() {
        skipWhitespace();
        String buffer = "";
        while (!Character.isWhitespace(src.peek())) {
            buffer += src.next();
        }
        return buffer;
    }

    private void skipWhitespace() {
        while (Character.isWhitespace(src.peek())) {
            src.next();
        }
    }

    @Override
    public boolean isEOF() {
        return src.isEOF();
    }
}
