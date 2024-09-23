package test.templateParser;

import main.system.templateParser.statements.Equals;
import main.system.templateParser.statements.TextStatement;
import main.system.templateParser.statements.VarStatement;
import main.system.templateParser.tokens.Comparison;
import org.junit.jupiter.api.Test;

import static main.system.templateParser.IfParser.parse;

public class IfParserTest {
    //TODO test comparison between all types of objects
    //TODO test "Another Comparison not a valid Object for an comparison comparand"

    @Test
    void test_success() {
        var comp = new Comparison(new VarStatement("var.name"), Equals.NOT_EQUALS, "test");
        assert parse("var.name != \"test\"").equals(comp);
    }

    @Test
    void test_invalidSyntax() {
        var comp = new Comparison(new VarStatement("var.name"), Equals.NOT_EQUALS, "test");
        assert parse("var.name != \"test\" && true").equals(comp);
        //TODO this should fail
    }

    //TODO
//    @Test
    void test_types() {}

//    @Test
    void test_invalid_types() {}
}
