package talium.system.templateParser;

import talium.system.templateParser.statements.*;
import talium.system.templateParser.tokens.Comparison;
import org.apache.commons.lang.NullArgumentException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static talium.system.templateParser.TemplateInterpreter.getNestedReplacement;
import static talium.system.templateParser.TemplateInterpreter.populate;
import static org.junit.jupiter.api.Assertions.fail;

public class TemplateInterpreterTest {
    static List<Statement> TEMPLATE_VAR = Arrays.asList(
            new TextStatement("Hello, "),
            new VarStatement("name"),
            new TextStatement("!")
    );
    static List<Statement> TEMPLATE_IF = Arrays.asList(
            new TextStatement("Hello, "),
            new IfStatement(new Comparison("var.name", Equals.NOT_EQUALS, "test"), List.of(
                    new VarStatement("var.name")
            ), List.of(
                    new TextStatement("unnamed")
            )),
            new TextStatement("!")
    );
    static List<Statement> TEMPLATE_LOOP = List.of(
            new TextStatement("Hello,"),
            new LoopStatement("loopVar", "varName", Arrays.asList(
                    new TextStatement(" "),
                    new VarStatement("varName")
            )),
            new TextStatement("!")
    );


    //TODO try iterating non list object
    @Test
    void iterate_non_iterable() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("loopVar", "this_is_not_a_list");
        System.out.println(populate(TEMPLATE_LOOP, map));
    }

    //TODO try vars
    @Test
    void var() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "dummy name");
        assert populate(TEMPLATE_VAR, map).equals("Hello, dummy name!");
    }

    //TODO try empty list
    @Test
    void iterate_empty_list() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("loopVar", new ArrayList<>());
        System.out.println(populate(TEMPLATE_LOOP, map));
    }

    //TODO try error if statement
    //TODO try nested loops

    @Nested
    class NestedReplacement {
        private record TestClass(String testString, int testInteger) {
        }

        @Test
        void string() throws NoSuchFieldException, IllegalAccessException {
            HashMap<String, Object> map = new HashMap<>();
            map.put("test", "testing String");
            map.put("dummyVar", 23899);
            assert getNestedReplacement("test", map).equals("testing String");
        }

        @Test
        void integer() throws NoSuchFieldException, IllegalAccessException {
            HashMap<String, Object> map = new HashMap<>();
            map.put("integerValue", 23899);
            map.put("dummyVar", "testing String");
            assert getNestedReplacement("integerValue", map).equals(23899);
        }

        @Test
        void object_path() throws NoSuchFieldException, IllegalAccessException {
            TestClass testClass = new TestClass("testString", 23899);
            HashMap<String, Object> map = new HashMap<>();
            map.put("testObject", testClass);
            map.put("dummyVar", "testing String");
            assert getNestedReplacement("testObject.testInteger", map).equals(23899);
        }

        @Test
        void list() throws NoSuchFieldException, IllegalAccessException {
            HashMap<String, Object> map = new HashMap<>();
            ArrayList<String> list = new ArrayList<>();
            list.add("dummyValue");
            list.add("dummyString");
            map.put("listVar", list);
            map.put("dummyVar", "testing String");
            assert getNestedReplacement("listVar", map).equals(list);
        }

        @Test
        void toplevel_null() throws NoSuchFieldException, IllegalAccessException {
            HashMap<String, Object> map = new HashMap<>();
            map.put("testObject", null);
            map.put("dummyVar", "testing String");
            try {
                System.out.println(getNestedReplacement("testObject.testInteger", map));
                fail("Should have thrown NullArgumentException");
            } catch (NullArgumentException _) {
            }
        }

        @Test
        void end_value_null() throws NoSuchFieldException, IllegalAccessException {
            TestClass testClass = new TestClass(null, 23899);
            HashMap<String, Object> map = new HashMap<>();
            map.put("testObject", testClass);
            map.put("dummyVar", "testing String");
            assert getNestedReplacement("testObject.testString", map) == null;
        }

        @Test
        void private_var() throws NoSuchFieldException, IllegalAccessException {
            class PrivateClass {
                private final String testString;
                final int testInteger;

                public PrivateClass(String testString, int testInteger) {
                    this.testString = testString;
                    this.testInteger = testInteger;
                }
            }
            PrivateClass testClass = new PrivateClass("testString", 23899);
            HashMap<String, Object> map = new HashMap<>();
            map.put("testObject", testClass);
            map.put("dummyVar", "testing String");
            assert getNestedReplacement("testObject.testString", map).equals("testString");
        }

        @Test
        void missing_field() throws IllegalAccessException {
            TestClass testClass = new TestClass("testString", 23899);
            HashMap<String, Object> map = new HashMap<>();
            map.put("testObject", testClass);
            map.put("dummyVar", "testing String");
            try {
                System.out.println(getNestedReplacement("testObject.testBoolean", map));
            } catch (NoSuchFieldException _) {
            }
        }
    }
}
