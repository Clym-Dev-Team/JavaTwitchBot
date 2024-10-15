package talium.system.templateParser;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

// the correct behaviour is not entirely specified, thats why this test is disabled by default.
// more or less: if a token is started it has to be finished, otherwise it is a syntax error
// completion of a statement is not yet checked at this stage, so a missing else directive is not an error at this stage
@Disabled
public class TemplateLexerTest {
    @Test
    void missing_closing_bracket() {
        TemplateLexer lex = new TemplateLexer("Hello, ${var.name!");
        System.out.println(lex.parse());
    }

    @Test
    void missing_dollar() {
        TemplateLexer lex = new TemplateLexer("Hello, {var.name}!");
        System.out.println(lex.parse());
    }

    @Test
    void missing_closing_bracket_directive() {
        TemplateLexer lex = new TemplateLexer("Hello, %{ if var.name != \"\" }${var.name}%{ else }unnamed%{ endif ");
        System.out.println(lex.parse());
    }

    @Test
    void missing_percent() {
        TemplateLexer lex = new TemplateLexer("Hello, %{ if var.name != \"\" }${var.name}%{ else }unnamed{ endif }");
        System.out.println(lex.parse());
    }

    @Test
    void malformed_endIf() {
        TemplateLexer lex = new TemplateLexer("Hello, %{ if var.name != \"\" }${var.name}%{ else }unnamed%{ endif dadasdadad }");
        System.out.println(lex.parse());
    }
}
