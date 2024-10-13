package stringTemplates;

import talium.system.stringTemplates.TemplateResolver;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class TemplateResolverTest {

    @Test
    void correctVarTemplate() {
        String template = "test command ${test.user.message} command ${test.user} text ende ${list}";
        HashMap<String, Object> variables = new HashMap<>();
        variables.put("varname", "test");
        ArrayList<String> list = new ArrayList<>();
        list.add("test");
        list.add("list item 1");
        variables.put("list", list);
        variables.put("test", new test("testField", "age1337", new user("jfk", "testmessage")));
        String out = TemplateResolver.populate(template, variables);
        System.out.println("out = " + out);
    }

    @Test
    void forTemplate() {
        String template = "test command text %{for item in list} item: \"${item}\" %{ endfor } ende ";
        HashMap<String, Object> variables = new HashMap<>();
        ArrayList<String> list = new ArrayList<>();
        list.add("test");
        list.add("list item 1");
        variables.put("list", list);
        String out = TemplateResolver.populate(template, variables);
        System.out.println("out = " + out);
    }

}


record test(String aField, String age, user user) {}

record user(String name, String message) {
    @Override
    public String toString() {
        return "user{" +
                "name='" + name + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}