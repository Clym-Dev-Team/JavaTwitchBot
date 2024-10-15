package talium.system;

import talium.inputs.Twitch4J.Twitch4JInput;
import talium.system.stringTemplates.Template;
import talium.system.templateParser.ArgumentValueNullException;
import talium.system.templateParser.UnIterableArgumentException;
import talium.system.templateParser.UnsupportedComparandType;
import talium.system.templateParser.UnsupportedComparisonOperator;
import talium.system.templateParser.majorParser.TemplateParser;
import talium.system.templateParser.statements.Statement;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static talium.system.templateParser.TemplateInterpreter.populate;

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
            String message;
            try {
                var parsed = new TemplateParser(template).parse();
                message = populate(parsed, values);
            } catch (UnsupportedComparisonOperator | NoSuchFieldException | ArgumentValueNullException |
                     IllegalAccessException | UnIterableArgumentException | UnsupportedComparandType e) {
                //TODO handle exceptions
                throw new RuntimeException(e);
            }
            Twitch4JInput.sendMessage(message);
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
