package main.system.stringTemplates.parser;

interface TokenStream<T> {
    T peek();

    T next();

    boolean isEOF();
}

class CharakterStream implements TokenStream<Character> {
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
}