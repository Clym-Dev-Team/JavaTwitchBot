package talium.system.templateParser;

import org.junit.jupiter.api.Test;
import talium.system.templateParser.majorParser.TemplateLexer;

// correct behaviour for now is to just fail semi silently and try to output as much as possible into the chat
// this tests if any of these parsing attempts throw an exception
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
