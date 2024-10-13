package talium.system.hookSystem;

import kotlin.text.MatchResult;
import kotlin.text.Regex;
import talium.system.commandSystem.repositories.Message;
import talium.system.commandSystem.repositories.TwitchUser;
import talium.system.commandSystem.repositories.TwitchUserPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;

/**
 * Parses a Command to find and execute any hooks that are used in it.
 */
public class HookParser {

    private static final Regex MATCH_TILL_CONTROL_CHAR = new Regex("^[^{}°]*");
    private static final Logger logger = LoggerFactory.getLogger(HookParser.class);

    public static void main(String[] args) {
//        new HookMethodRunner("main.modules");
        TwitchUser user = new TwitchUser("427320589", "orciument", 48, 1, TwitchUserPermission.OWNER);
        Message message = new Message("", "!irgendwas das ist der originale message text", user, false, false, false, false, null, null, "427320589", Instant.now());
        System.out.println(parseCommand(message, "test", "Das ist ein Test Command {follow {currentTime}  {somepreset} °das ist ein beispiel text der im command landet°} danke fürs zuhören!"));
        //TODO Hook needs one character bevor start to be detected!
        System.out.println(parseCommand(message, "test", "{follow {currentTime}  {somepreset} °das ist ein beispiel text der im command landet°}"));
        System.out.println(parseCommand(message, "test", " {follow {currentTime}  {somepreset} °das ist ein beispiel text der im command landet°}"));
        System.out.println("Output: " + parseCommand(message, "test", "Das ist ein Test Command {Math °+° {senderSubMonths} {randomInt} } danke fürs zuhören!  @{sender}: {currentTime}"));
        System.out.println(parseCommand(message, "test", "Das ist ein Test Command {Math °+° {senderSubMonths} {randomInt} }"));
        System.out.println(parseCommand(message, "test", "Das ist ein Test Command {Math °+° {senderSubMonths} {randomInt} °dasd °}"));
        System.out.println(parseCommand(message, "Test", "Test: {Math °+° {follow} {randomInt}}"));
    }

    /**
     * Takes Command and parses and evaluates all Hooks contained in the Command, and returns it ready o be printed into chat
     *
     * @param originalMessage Message that triggered the Hook, as context
     * @param uniqueName      uniqueName of the Command, for error handling
     * @param commandText     commandText of the Command, will be evaluated
     * @return evaluated Command with hook outputs
     */
    public static String parseCommand(Message originalMessage, String uniqueName, String commandText) {
        if (!equalBracketNumber(commandText)) {
            logger.error("Command: {}: Missing one or more closing brackets!", uniqueName);
            return "{ERROR}";
        }

        String output = "";
        int startHookIndex = commandText.indexOf('{');
        while (startHookIndex >= 0) {
            //Add part before start of Hook to output without modifying it
            output += commandText.substring(0, startHookIndex);

            int endHookIndex = findClosingBracket(commandText);
            String hookString = commandText.substring(startHookIndex, endHookIndex + 1);

            //Parse the substring Hook
            output += parseHooks(originalMessage, hookString);

            //Delete already parsed part from Input
            commandText = commandText.substring(endHookIndex + 1);
            startHookIndex = commandText.indexOf('{');
        }
        return output + commandText;
    }

    /**
     * Parse an entire Top Level Hook String, parses it and executes the Hooks. <br/>
     * Returns "{ERROR}" if any hook encountered any Error, the error is logged to the Console.
     *
     * @param originalMessage Message that triggered the Hook, as context
     * @param remainingHook   The Hook String in the Command. e.g. "{testHook {randomInt} °hello°}"
     * @return the Hook output
     */
    private static String parseHooks(Message originalMessage, String remainingHook) {
        //Skip first {
        remainingHook = remainingHook.substring(1);

        String name;
        ArrayList<String> parameter = new ArrayList<>();

        //Has a parameter
        name = textTillControlChar(remainingHook).trim();
        //Remove name from the hook String
        remainingHook = remainingHook.replace(name, "");

        while (!remainingHook.isEmpty()) {
            switch (remainingHook.charAt(0)) {
                //Parameter is Hook
                case '{' -> {
                    //Find Part of the String that represents the Hook that is the Parameter
                    int endIndex = findClosingBracket(remainingHook);
                    String substring = remainingHook.substring(0, endIndex + 1);

                    //Parse the Hook recursive
                    String returnString = parseHooks(originalMessage, substring);

                    //propagate Error up the call chain
                    if (returnString.trim().equalsIgnoreCase("{ERROR}")) return "{ERROR}";

                    //Add to Parameter Map
                    parameter.add(returnString);

                    //Remove parsed part from String
                    remainingHook = remainingHook.replace(substring, "");
                }
                //Parameter is String
                case '°' -> {
                    int endIndex = remainingHook.substring(1).indexOf("°") + 1;
                    String substring = remainingHook.substring(1, endIndex);

                    //propagate Error up the call chain
                    if (substring.trim().equalsIgnoreCase("{ERROR}")) return "{ERROR}";

                    //Add to Parameter Map
                    parameter.add(substring);

                    //Remove parsed part from String
                    remainingHook = remainingHook.replace("°" + substring + "°", "");
                }
                case '}', ' ' -> remainingHook = remainingHook.substring(1);
                default -> remainingHook = remainingHook.substring(1);
            }
        }
        //Execute hook
        return HookMethodRunner.runHook(name, originalMessage, parameter);
    }

    /**
     * @param input string with (closing) control Char ('}','°')
     * @return string until next control char, exclusive
     */
    private static String textTillControlChar(String input) {
        MatchResult result = MATCH_TILL_CONTROL_CHAR.find(input, 0);
        // There should always be a next control Char, because we checked for missing brackets beforehand
        assert result != null;
        return result.getValue();
    }

    /**
     * Counts the opening and closing brackets and returns the index of the closing bracket. If no closing bracket could be found, 0 is returned.
     * @apiNote Expects that the first character of the String is the first opening bracket. So be careful about leading whitespace .
     * @param input
     * @return The Index of the closing bracket. If no closing bracket could be found, 0 is returned
     */
    private static int findClosingBracket(String input) {
        int start = input.indexOf('{') + 1;
        int depth = 1;

        for (int i = start; i < input.toCharArray().length; i++) {
            char c = input.toCharArray()[i];
            switch (c) {
                case '{' -> depth++;
                case '}' -> depth--;
            }
            if (depth == 0) return i;
        }
        return 0;
    }

    /**
     * @param input The String to check
     * @return If the String contains a equal number of opening and closing brackets
     */
    private static boolean equalBracketNumber(String input) {
        int open = 0;
        int close = 0;

        for (char aChar : input.toCharArray()) {
            if (aChar == '{') {
                open++;
            } else if (aChar == '}') {
                close++;
            }
        }
        return open == close;
    }
}
