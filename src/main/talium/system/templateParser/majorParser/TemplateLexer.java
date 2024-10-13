package talium.system.templateParser.majorParser;


import talium.system.templateParser.CharakterStream;
import talium.system.templateParser.TokenStream;
import talium.system.templateParser.tokens.TemplateTokenKind;
import talium.system.templateParser.tokens.TemplateToken;

import java.util.ArrayList;
import java.util.List;
/**
 * A Lexer for String Templates that consumes a CharacterStream and provides the tokens as a TokenStream
 */
public class TemplateLexer implements TokenStream<TemplateToken> {
    CharakterStream src;
    List<TemplateToken> tokens;
    int pos;

    public TemplateLexer(String src) {
        this.src = new CharakterStream(src);
        this.tokens = new ArrayList<>();
    }

    /**
     * performs the first parse pass and returns a list of major tokens
     * @return list of major tokens
     */
    public List<TemplateToken> parse() {
        List<TemplateToken> list = new ArrayList<>();
        while (!src.isEOF()) {
            TemplateToken next = next();
            if (next == null) {
                break;
            }
            list.add(next);
        }
        return list;
    }

    @Override
    public TemplateToken peek() {
        if (pos >= tokens.size()) {
            tokens.add(parseToken());
        }
        return tokens.get(pos);
    }

    @Override
    public TemplateToken next() {
        if (pos >= tokens.size()) {
            tokens.add(parseToken());
        }
        TemplateToken t = tokens.get(pos);
        pos += 1;
        return t;
    }

    /**
     * consume a singe major token from the character stream
     * @return the next token
     */
    private TemplateToken parseToken() {
        if (src.isEOF()) {
            return null;
        }
        if (src.peek() == '$' && src.future() == '{') {
            src.next(); // consume $
            src.next(); // consume {
            String varName = src.readUntil('}');
            src.next(); //consume }
            return new TemplateToken(TemplateTokenKind.VAR, varName);

        } else if (src.peek() == '%' && src.future() == '{') {
            // Get type of directive if or for
            src.next(); //consume %
            src.next(); //consume {
            String directive = src.readTillWhitespace();
            src.skipWhitespace();

            if (directive.equals("if")) {
                String condition = src.readUntil('}');
                src.skipWhitespace();
                src.next(); //consume }
                return new TemplateToken(TemplateTokenKind.IF_HEAD, condition);
            } else if (directive.equals("else")) {
                src.skipWhitespace();
                src.next(); //consume }
                return new TemplateToken(TemplateTokenKind.IF_ELSE, "");
            } else if (directive.equals("endif")) {
                src.skipWhitespace();
                src.next(); //consume }
                return new TemplateToken(TemplateTokenKind.IF_END, "");
            } else if (directive.equals("for")) {
                String head = src.readUntil('}');
                src.skipWhitespace();
                src.next(); //consume }
                return new TemplateToken(TemplateTokenKind.FOR_HEAD, head);
            } else if (directive.equals("endfor")) {
                src.skipWhitespace();
                src.next(); //consume }
                return new TemplateToken(TemplateTokenKind.FOR_END, "");
            } else {
                throw new RuntimeException(STR."Directive not supported: \{directive}");
            }
        } else {
            String buffer = "";
            // while no var or directive is encountered, all chars are consumed into a TEXT token
            while (!src.isEOF() && src.peek() != '$' && src.peek() != '%') {
                buffer += src.next();
            }
            return new TemplateToken(TemplateTokenKind.TEXT, buffer);
        }
    }

    @Override
    public boolean isEOF() {
        return src.isEOF();
    }
}