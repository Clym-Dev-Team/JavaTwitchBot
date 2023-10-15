package main.system.hookSystem;

import kotlin.text.MatchResult;
import kotlin.text.Regex;
import main.system.commandSystem.repositories.Message;
import main.system.commandSystem.repositories.TwitchUser;
import main.system.commandSystem.repositories.TwitchUserPermission;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Parses a Command to find and execute any hooks that are used in it.
 */
public class HookParser {
    public static void main(String[] args) {
        new HookMethodRunner();
        TwitchUser user = new TwitchUser("427320589", "orciument", 48, 1, TwitchUserPermission.OWNER);
        Message message = new Message("","!irgendwas das ist der originale message text", user, false, false, false,false, null,null, "427320589", Instant.now());
//        System.out.println(parseCommand(message,"Das ist ein Test Command {follow {currentTime}  {somepreset} °das ist ein beispiel text der im command landet°} danke fürs zuhören!"));
        //TODO Hook needs one character bevor start to be detected!
//        System.out.println(parseCommand(message,"{follow {currentTime}  {somepreset} °das ist ein beispiel text der im command landet°}"));
//        System.out.println(parseCommand(message," {follow {currentTime}  {somepreset} °das ist ein beispiel text der im command landet°}"));
//        System.out.println("Output: "+ parseCommand(message,"Das ist ein Test Command {Math °+° {senderSubMonths} {randomInt} } danke fürs zuhören!  @{sender}: {currentTime}"));
//        System.out.println(parseCommand(message,"Das ist ein Test Command {Math °+° {senderSubMonths} {randomInt} }"));
//        System.out.println(parseCommand(message,"Das ist ein Test Command {Math °+° {senderSubMonths} {randomInt} °dasd °}"));
        System.out.println(parseCommand(message, "Test: {Math °+° {follow} {randomInt}}"));
    }

    /**
     * Parse a Command for Hooks and execute all the Hooks
     * @param message The Message which Hooks should be executed
     * @param input The Message that triggered the Command and Arguments come from
     * @return The Response to the Message/the build Command Response
     */
    public static String parseCommand(Message originalMessage, String input) {
        if (!equalBracketNumber(input))
            //TODO Improve
            return "ERROR Alle geöffneten Klammern müssen wieder geschlossen werden!";
        //TODO LOGGER
//        System.out.println("Input: " + input);

        String output = "";
        int startHookIndex = input.indexOf('{');
        while (startHookIndex > 0) {
            //Add part before start of Hook to output without modifying it
            output += input.substring(0, startHookIndex);

            int endHookIndex = findClosingBracket(input);
            String substring = input.substring(startHookIndex, endHookIndex + 1);

            //Parse the substring Hook
            output += parseHooks(originalMessage,substring);

            //Delete already parsed part from Input
            input = input.substring(endHookIndex + 1);
            startHookIndex = input.indexOf('{');
        }
        return output + input;
    }

    /**
     * Parse the given Hook String, find the matching Hook function, match all the Arguments, and execute the Hook function
     * @param message The Message that triggered the Command
     * @param hook The Hook String from the Command
     * @return The Output from the Hook
     */
    private static String parseHooks(Message message, String hook) {
        //Skip first {
        hook = hook.substring(1);

        String name;
        ArrayList<String> parameter = new ArrayList<>();

        //Has a parameter
        name = textTillControlChar(hook).trim();
        //Remove name from the hook String
        hook = hook.replace(name, "");

        while (hook.length() > 0) {
            switch (hook.charAt(0)) {
                //Parameter is Hook
                case '{' -> {
                    //Find Part of the String that represents the Hook that is the Parameter
                    int endIndex = findClosingBracket(hook);
                    String substring = hook.substring(0, endIndex + 1);

                    //Parse the Hook recursive
                    String returnString = parseHooks(message, substring);

                    //Add to Parameter Map
                    parameter.add(returnString);

                    //Remove parsed part from String
                    hook = hook.replace(substring, "");
                }
                //Parameter is String
                case '°' -> {
                    int endIndex = hook.substring(1).indexOf("°") + 1;
                    String substring = hook.substring(1, endIndex);

                    //Add to Parameter Map
                    parameter.add(substring);

                    //Remove parsed part from String
                    hook = hook.replace("°" + substring + "°", "");
                }
                case '}', ' ' -> hook = hook.substring(1);
                //                default -> hook = hook.substring(1);
            }
        }
        //Execute hook
        return HookMethodRunner.runHook(name, message, parameter);
    }

    private static String textTillControlChar(String input) {
        Regex regex = new Regex("^[^{}°]*");
        MatchResult result = regex.find(input, 0);
        assert result != null;
        return result.getValue();
    }

    /**
     * Counts the opening and closing brackets and returns the index of the closing bracket.
     * @apiNote Expects that the first character of the String is the first opening bracket. So be careful about leading whitespace .
     * @param input
     * @return The Index of the closing bracket
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
            if (depth == 0)
                return i;
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
