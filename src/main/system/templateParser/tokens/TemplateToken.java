package main.system.templateParser.tokens;

/**
 * Major (primary) token Object, used for first parser pass.
 * This object only stores the entire heads of if and for blocks for further processing, these sections are parsed later.
 * @param kind what kind of token this is
 * @param value string value of the token
 */
public record TemplateToken(TemplateTokenKind kind, String value) {
    @Override
    public String toString() {
        return STR."\{kind}(\{value})";
    }
}