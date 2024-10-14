package talium.system.templateParser;

import talium.system.templateParser.ifParser.IfInterpreter;
import talium.system.templateParser.UnsupportedComparandType;
import talium.system.templateParser.statements.Equals;
import talium.system.templateParser.tokens.Comparison;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.fail;

public class IfInterpreterTest {

    @Nested
    class OperatorTests {
        @Test
        void ints() {
            assert IfInterpreter.compare(new Comparison(34993497, Equals.NOT_EQUALS, 348));
            assert !IfInterpreter.compare(new Comparison(34993497, Equals.NOT_EQUALS, 34993497));

            assert IfInterpreter.compare(new Comparison(34993497, Equals.EQUALS, 34993497));
            assert !IfInterpreter.compare(new Comparison(34993497, Equals.EQUALS, 348));

            assert IfInterpreter.compare(new Comparison(348, Equals.LESS_THAN, 34993497));
            assert !IfInterpreter.compare(new Comparison(34993497, Equals.LESS_THAN, 34993497));

            assert IfInterpreter.compare(new Comparison(348, Equals.LESS_THAN_OR_EQUALS, 34993497));
            assert !IfInterpreter.compare(new Comparison(34993497, Equals.LESS_THAN_OR_EQUALS, 348));
            assert IfInterpreter.compare(new Comparison(34993497, Equals.LESS_THAN_OR_EQUALS, 34993497));

            assert IfInterpreter.compare(new Comparison(34993497, Equals.GREATER_THAN, 348));
            assert !IfInterpreter.compare(new Comparison(348, Equals.GREATER_THAN, 34993497));

            assert IfInterpreter.compare(new Comparison(34993497, Equals.GREATER_THAN_OR_EQUALS, 348));
            assert !IfInterpreter.compare(new Comparison(348, Equals.GREATER_THAN_OR_EQUALS, 34993497));
            assert IfInterpreter.compare(new Comparison(34993497, Equals.GREATER_THAN_OR_EQUALS, 34993497));
        }

        @Test
        void longs() {
            assert IfInterpreter.compare(new Comparison(6323833349178L, Equals.NOT_EQUALS, 19282L));
            assert !IfInterpreter.compare(new Comparison(6323833349178L, Equals.NOT_EQUALS, 6323833349178L));

            assert IfInterpreter.compare(new Comparison(6323833349178L, Equals.EQUALS, 6323833349178L));
            assert !IfInterpreter.compare(new Comparison(6323833349178L, Equals.EQUALS, 19282L));

            assert IfInterpreter.compare(new Comparison(19282L, Equals.LESS_THAN, 6323833349178L));
            assert !IfInterpreter.compare(new Comparison(6323833349178L, Equals.LESS_THAN, 6323833349178L));

            assert IfInterpreter.compare(new Comparison(19282L, Equals.LESS_THAN_OR_EQUALS, 6323833349178L));
            assert !IfInterpreter.compare(new Comparison(6323833349178L, Equals.LESS_THAN_OR_EQUALS, 19282L));
            assert IfInterpreter.compare(new Comparison(6323833349178L, Equals.LESS_THAN_OR_EQUALS, 6323833349178L));

            assert IfInterpreter.compare(new Comparison(6323833349178L, Equals.GREATER_THAN, 19282L));
            assert !IfInterpreter.compare(new Comparison(19282L, Equals.GREATER_THAN, 6323833349178L));

            assert IfInterpreter.compare(new Comparison(6323833349178L, Equals.GREATER_THAN_OR_EQUALS, 19282L));
            assert !IfInterpreter.compare(new Comparison(19282L, Equals.GREATER_THAN_OR_EQUALS, 6323833349178L));
            assert IfInterpreter.compare(new Comparison(6323833349178L, Equals.GREATER_THAN_OR_EQUALS, 6323833349178L));
        }
        @Test
        void floats() {
            assert IfInterpreter.compare(new Comparison(687.000033339284F, Equals.NOT_EQUALS, 12.28434F));
            assert !IfInterpreter.compare(new Comparison(687.000033339284F, Equals.NOT_EQUALS, 687.000033339284F));

            assert IfInterpreter.compare(new Comparison(687.000033339284F, Equals.EQUALS, 687.000033339284F));
            assert !IfInterpreter.compare(new Comparison(687.000033339284F, Equals.EQUALS, 12.28434F));

            assert IfInterpreter.compare(new Comparison(12.28434F, Equals.LESS_THAN, 687.000033339284F));
            assert !IfInterpreter.compare(new Comparison(687.000033339284F, Equals.LESS_THAN, 687.000033339284F));

            assert IfInterpreter.compare(new Comparison(12.28434F, Equals.LESS_THAN_OR_EQUALS, 687.000033339284F));
            assert !IfInterpreter.compare(new Comparison(687.000033339284F, Equals.LESS_THAN_OR_EQUALS, 12.28434F));
            assert IfInterpreter.compare(new Comparison(687.000033339284F, Equals.LESS_THAN_OR_EQUALS, 687.000033339284F));

            assert IfInterpreter.compare(new Comparison(687.000033339284F, Equals.GREATER_THAN, 12.28434F));
            assert !IfInterpreter.compare(new Comparison(12.28434F, Equals.GREATER_THAN, 687.000033339284F));

            assert IfInterpreter.compare(new Comparison(687.000033339284F, Equals.GREATER_THAN_OR_EQUALS, 12.28434F));
            assert !IfInterpreter.compare(new Comparison(12.28434F, Equals.GREATER_THAN_OR_EQUALS, 687.000033339284F));
            assert IfInterpreter.compare(new Comparison(687.000033339284F, Equals.GREATER_THAN_OR_EQUALS, 687.000033339284F));
        }
    }

    @Nested
    class TypeTests {
        @Test
        void boolean_boolean() {
            assert IfInterpreter.compare(new Comparison(true, Equals.NOT_EQUALS, false));
            assert IfInterpreter.compare(new Comparison(false, Equals.EQUALS, false));
            assert !IfInterpreter.compare(new Comparison(false, Equals.EQUALS, true));
        }

        @Test
        void string_string() {
            assert IfInterpreter.compare(new Comparison("test", Equals.NOT_EQUALS, "wort"));
            assert IfInterpreter.compare(new Comparison("test", Equals.EQUALS, "test"));
            assert !IfInterpreter.compare(new Comparison("test", Equals.EQUALS, "wort"));
        }

        @Test
        void int_int() {
            assert IfInterpreter.compare(new Comparison(10, Equals.NOT_EQUALS, 11));
            assert IfInterpreter.compare(new Comparison(10, Equals.EQUALS, 10));
            assert !IfInterpreter.compare(new Comparison(10, Equals.EQUALS, 11));
        }

        @Test
        void long_long() {
            assert IfInterpreter.compare(new Comparison(10L, Equals.NOT_EQUALS, 11L));
            assert IfInterpreter.compare(new Comparison(10L, Equals.EQUALS, 10L));
            assert !IfInterpreter.compare(new Comparison(10L, Equals.EQUALS, 11L));
        }

        @Test
        void float_float() {
            assert IfInterpreter.compare(new Comparison(10.32323F, Equals.NOT_EQUALS, 10.52323F));
            assert IfInterpreter.compare(new Comparison(10.32323F, Equals.EQUALS, 10.32323F));
            assert !IfInterpreter.compare(new Comparison(10.32323F, Equals.EQUALS, 10.52323F));
        }

        @Test
        void double_double() {
            assert IfInterpreter.compare(new Comparison(10.32323D, Equals.NOT_EQUALS, 10.52323D));
            assert IfInterpreter.compare(new Comparison(10.32323D, Equals.EQUALS, 10.32323D));
            assert !IfInterpreter.compare(new Comparison(10.32323D, Equals.EQUALS, 10.52323D));
        }

        @Test
        void char_char() {
            assert IfInterpreter.compare(new Comparison('t', Equals.NOT_EQUALS, 'w'));
            assert IfInterpreter.compare(new Comparison('t', Equals.EQUALS, 't'));
            assert !IfInterpreter.compare(new Comparison('t', Equals.EQUALS, 'w'));
        }

        @Test
        void int_long() {
            assert IfInterpreter.compare(new Comparison(10, Equals.NOT_EQUALS, 11L));
            assert IfInterpreter.compare(new Comparison(10, Equals.EQUALS, 10));
            assert !IfInterpreter.compare(new Comparison(10, Equals.EQUALS, 11L));
        }

        @Test
        void float_double() {
            assert IfInterpreter.compare(new Comparison(10.32323F, Equals.NOT_EQUALS, 10.52323D));
            assert IfInterpreter.compare(new Comparison(10.32323F, Equals.EQUALS, 10.32323F));
            assert !IfInterpreter.compare(new Comparison(10.32323F, Equals.EQUALS, 10.52323D));
        }

        @Test
        void string_int() {
            try {
                IfInterpreter.compare(new Comparison("test", Equals.EQUALS, 1));
                fail("UnsupportedOperationException not thrown!");
            } catch (UnsupportedComparandType _) {}
        }

        @Test
        void string_double() {
            try {
                IfInterpreter.compare(new Comparison("test", Equals.EQUALS, 10.52323D));
                fail("UnsupportedOperationException not thrown!");
            } catch (UnsupportedComparandType _) {}
        }

        @Test
        void string_boolean() {
            try {
                IfInterpreter.compare(new Comparison("test", Equals.EQUALS, true));
                fail("UnsupportedOperationException not thrown!");
            } catch (UnsupportedComparandType _) {}
        }

        @Test
        void int_boolean() {
            try {
                IfInterpreter.compare(new Comparison(123, Equals.EQUALS, true));
                fail("UnsupportedOperationException not thrown!");
            } catch (UnsupportedComparandType _) {}
        }

        @Test
        void long_float() {
            try {
                IfInterpreter.compare(new Comparison(10L, Equals.NOT_EQUALS, 10.52323F));
                fail("UnsupportedOperationException not thrown!");
            } catch (UnsupportedComparandType _) {}
        }

        @Test
        void object_object() {
            try {
                IfInterpreter.compare(new Comparison(123, Equals.EQUALS, new ArrayList<Exception>()));
                fail("UnsupportedOperationException not thrown!");
            } catch (UnsupportedComparandType _) {}
        }

        @Test
        void object_boolean() {
            try {
                IfInterpreter.compare(new Comparison(new ArrayList<Exception>(), Equals.EQUALS, true));
                fail("UnsupportedOperationException not thrown!");
            } catch (UnsupportedComparandType _) {}
        }
    }
}
