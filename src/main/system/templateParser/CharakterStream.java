package main.system.templateParser;

/**
 * A indexed Stream of characters for parsing.
 */
public class CharakterStream implements TokenStream<Character> {
    String src;
    int pos;

    public CharakterStream(String src) {
        this.src = src;
    }

    @Override
    public Character peek() {
        return src.charAt(pos);
    }

    @Override
    public Character next() {
        char c = src.charAt(pos);
        pos += 1;
        return c;
    }

    /**
     * peeks the character after the next one
     * @return the character after the next one
     */
    public Character future() {
        if (pos + 1 >= src.length()) {
            return ' ';
        }
        return src.charAt(pos + 1);
    }

    @Override
    public boolean isEOF() {
        return pos == src.length();
    }

    /**
     * consume all characters up to and excluding the first found target character
     * @param c target character
     * @return return all consumed characters up to the target
     */
    public String readUntil(char c) {
        skipWhitespace();
        String buffer = "";
        boolean escapeNext = false;
        while (peek() != c || escapeNext) {
            if (peek() == '\\') { //TODO this impossible, we need to save separately if the last char was a -> \ (or check in the buffer) (also, if \ is the target char, we should exit)
                escapeNext = true;
            }
            if (escapeNext) {
                escapeNext = false;
            }
            buffer += next();
        }
        return buffer;
    }

    /**
     * consume all characters up to and excluding the first character that is not a digit
     * @return return all consumed characters up to the non digit char
     * @see Character#isDigit(char)
     */
    public String readTillNotDigit() {
        skipWhitespace();
        String buffer = "";
        while (!Character.isDigit(peek())) {
            buffer += next();
        }
        return buffer;
    }

    /**
     * consume all characters up to and excluding the first character that is whitespace
     * @return return all consumed characters up to the whitespace
     * @see Character#isWhitespace(char)
     */
    public String readTillWhitespace() {
        skipWhitespace();
        String buffer = "";
        while (!isEOF() && !Character.isWhitespace(peek())) {
            buffer += next();
        }
        return buffer;
    }

    /**
     * consumes all characters until a non whitespace character is encountered
     */
    public void skipWhitespace() {
        while (!isEOF() && Character.isWhitespace(peek())) {
            pos += 1;
        }
    }
}