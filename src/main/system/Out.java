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

        public static void sendNamedTemplate(String module, String name, String objectName, HashMap<String, Object> baseValues) {
            Optional<Template> template = Template.repo.findByTypeAndNameAndObjectName(module, name, objectName);
            if (template.isEmpty()) {
                //TODO error handling, throw
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
