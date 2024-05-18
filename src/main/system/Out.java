package main.system;

import main.inputs.Twitch4J.Twitch4JInput;
import main.system.stringTemplates.Template;
import main.system.stringTemplates.TemplateResolver;

import java.util.HashMap;
import java.util.Optional;

public class Out {

    public static class Twitch {

        void sendRawMessage(String message) {
            Twitch4JInput.sendMessage(message);
        }

        void sendNamedTemplate(String type, String name, HashMap<String, Object> baseValues) {
            Optional<Template> template = Template.repo.findByTypeAndName(type, name);
            if (template.isEmpty()) {
                //TODO error handling, throw
                return;
            }
            //TODO resolve additional Contexts
            sendRawTemplate(template.get().template, baseValues);
        }

        void sendRawTemplate(String template, HashMap<String, Object> values) {
            String message = TemplateResolver.populate(template, values);
            Twitch4JInput.sendMessage(message);
        }
    }

    public static class Discord {}
    public static class Alert {}
}
