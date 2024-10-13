package hooksystem;

import talium.system.commandSystem.repositories.Message;
import talium.system.hookSystem.Hook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestHooks {

    private static final Logger logger = LoggerFactory.getLogger(TestHooks.class);

    @Hook
    public static String Math(String operation, String value1, String value2) {
        int int1;
        int int2;
        try {
            int1 = Integer.parseInt(value1);
        } catch (NumberFormatException e) {
            logger.error("Hook Math failed to parse value1 = {} into int", value1);
            return "{ERROR}";
        }
        try {
            int2 = Integer.parseInt(value2);
        } catch (NumberFormatException e) {
            logger.error("Hook Math failed to parse value2 = {} into int", value2);
            return "{ERROR}";
        }

        return switch (operation) {
            case "+" -> String.valueOf(int1 + int2);
            case "-" -> String.valueOf(int1 - int2);
            case "*" -> String.valueOf(int1 * int2);
            case "/" -> String.valueOf(int1 / int2);
            case "%" -> String.valueOf(int1 % int2);
            case "^" -> String.valueOf(Math.pow(int1, int2));
            default -> "0";
        };
    }

    @Hook
    public static String echo(String input) {
        return input;
    }

    @Hook
    public static String sender(Message message) {
        return message.user().name();
    }

    @Hook
    public static String senderSubMonths(Message message) {
        return String.valueOf(message.user().subscriberMonths());
    }
}
