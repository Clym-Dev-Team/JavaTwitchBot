package main.system;

import main.inputs.Twitch4J.Twitch4JInput;
import main.system.stringTemplates.Template;
import main.system.stringTemplates.TemplateResolver;

import java.util.HashMap;
import java.util.Optional;

public class Out {

    public static class Twitch {

        public static void sendRawMessage(String message) {
            Twitch4JInput.sendMessage(message);
        }

        public static void sendNamedTemplate(String module, String type, String object, HashMap<String, Object> baseValues) {
            Optional<Template> template = Template.repo.findByModuleAndTypeAndObject(module, type, object);
            if (template.isEmpty()) {
                //TODO error handling, throw
                System.err.println("no template found for module " + module + " and type " + type + " and object " + object);
                //TODO emit error as event
                return;
            }
            //TODO resolve additional Contexts
            sendRawTemplate(template.get().template, baseValues);
        }

        public static void sendRawTemplate(String template, HashMap<String, Object> values) {
            String message = TemplateResolver.populate(template, values);
            Twitch4JInput.sendMessage(message);
        }
    }

    public static class Discord {}
    public static class Alert {}
}
