package talium.system;

import talium.inputs.Twitch4J.Twitch4JInput;
import talium.system.stringTemplates.Template;
import talium.system.templateParser.exeptions.ArgumentValueNullException;
import talium.system.templateParser.exeptions.UnIterableArgumentException;
import talium.system.templateParser.exeptions.UnsupportedComparandType;
import talium.system.templateParser.exeptions.UnsupportedComparisonOperator;
import talium.system.templateParser.TemplateParser;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;

import static talium.system.templateParser.TemplateInterpreter.populate;

public class Out {

    public static class Twitch {

        public static void sendRawMessage(String message) {
            Twitch4JInput.sendMessage(message);
        }

        public static String sendNamedTemplate(String module, String type, String object, HashMap<String, Object> baseValues) throws NoSuchElementException {
            Optional<Template> template = Template.repo.findByModuleAndTypeAndObject(module, type, object);
            if (template.isEmpty()) {
                throw new NoSuchElementException(STR."no template found for module \{module} and type \{type} and object \{object}");
                //TODO emit error as event
                //edit: ^^ not sure why we would need to do this. Just output error into console/webconsole clearly our caller has no fucking idea what they want, so we shouldn't even throw. There is no way they could fix this
            }
            //TODO resolve additional Contexts
            return sendRawTemplate(template.get().template, baseValues);
        }

        public static String sendRawTemplate(String template, HashMap<String, Object> values) {
            String message;
            try {
                var parsed = new TemplateParser(template).parse();
                message = populate(parsed, values);
            } catch (UnsupportedComparisonOperator | NoSuchFieldException | ArgumentValueNullException |
                     IllegalAccessException | UnIterableArgumentException | UnsupportedComparandType e) {
                //TODO handle exceptions
                // the exceptions should be displayed in the console and in the webconsole with a fairly high priority
                throw new RuntimeException(e);
            }
            Twitch4JInput.sendMessage(message);
            return message;
        }
    }

    public static class Discord {
    }

    public static class Alert {
    }

    /**
     * Used for errors that should be displayed via a popup message in the panel
     */
    public static class MajorError {
    }

    /**
     * Errors important enough to warrant email alerting
     */
    public static class CriticalError {
    }
}
