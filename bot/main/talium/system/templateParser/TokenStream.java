package talium.system.templateParser;
/**
 * A generic indexed Stream of Objects for parsing.
 */
public interface TokenStream<T> {
    /**
     * returns the next token, without increasing the index
     */
    T peek();

    /**
     * returns the next token, increases the index by one token
     */
    T next();

    /**
     * Indicates end-of-file or end of the tokenStream
     */
    boolean isEOF();
}
