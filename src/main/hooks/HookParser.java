package hooks;

import kotlin.text.MatchResult;
import kotlin.text.Regex;

import java.util.ArrayList;
import java.util.HashMap;

public class HookParser {
    public static void main(String[] args) {
//        parseCommand("Das ist ein Test Command {follow {currentTime}  {somepreset} °das ist ein beispiel text der im command landet°} danke fürs zuhören!");
//        parseCommand("{follow {currentTime}  {somepreset} °das ist ein beispiel text der im command landet°}");
//        System.out.println("ergebnis:: " + prese("Das ist ein Test Command {follow {currentTime}  {somepreset} °das ist ein beispiel text der im command landet°} danke fürs zuhören!"));
//        System.out.println(prese("follow {currentTime}  {somepreset} °das ist ein beispiel text der im command landet°"));
//        System.out.println(parse3("follow {currentTime}  {somepreset} °das ist ein beispiel text der im command landet°"));
//        System.out.println(findClosingBracket("{follow {currentTime}  {somepreset} °das ist ein beispiel text der im command landet°}"));
//        System.out.println(findClosingBracket("Das ist ein Test Command {follow {currentTime}  {somepreset} °das ist ein beispiel text der im command landet°} danke fürs zuhören"));
//        System.out.println(parse4("Das ist ein Test Command {follow {currentTime}  {somepreset} °das ist ein beispiel text der im command landet°} danke fürs zuhören!  {ifequals {messageSource} °User° {atUser} } "));
        System.out.println(parse4("Das ist ein Test Command {follow {currentTime {getCurrentTimeZone {calcFormat °HH:FFMWW:SSS° } } }  {somepreset}  °das ist ein beispiel text der im command landet° } danke fürs zuhören!  {ifequals {messageSource}  °User°  {atUser} }"));
    }

    public static String parseCommand(String input) {
        if (!input.contains("{"))
            return input;

        ArrayList<HookString> list = new ArrayList<>();
//        String rest = "";
        int depth = 0;

        for (int i = 0, charArrayLength = input.toCharArray().length; i < charArrayLength; i++) {

            char c = input.toCharArray()[i];
            switch (c) {
                case '{' -> {
                    depth++;
//                    if (rest.length() != 0)
//                        list.add(new HookString(rest));
//                    list.add(new HookString()));
                    //mache nen Command Block auf
                }
                case '}' -> {
                    depth--;
                    //Schließe aktuellen Command block und "schicke" ihn ab, pack seinen Return Value an seine Stelle
                }
                default -> {
//                    if (depth == 0)
//                        rest += c;
                }
            }


//        Stack<Integer> braceIndex = new Stack<>();
//            char c = input.toCharArray()[i];
//            switch (c) {
//                case '{' -> {
//                    braceIndex.add(i);
            //mache nen Command Block auf
//                }
//                case '}' -> {
            //Schließe aktuellen Command block und "schicke" ihn ab, pack seinen Return Value an seine Stelle
//                    int start = braceIndex.pop() + 1;
//                    int end = i;
//                    String substring = input.substring(start, end);
//                    parseCommand(substring);
//
//                    System.out.println("input = " + input);
//                    System.out.println("Found segment from: " + start + " -> " + end + ": " + substring);
//                    System.out.println("---------------");
//                }
//            }
        }
        return null;
    }

    record Return(
            int continueIndex,
            String returnValue
    ) {
    }

    public static Return parse1(int startIndex, String input) {
//        System.out.println("input = " + input.substring(startIndex));
        String name = "";
        int state = 0;
        int start = 0;
        for (int i = startIndex, charArrayLength = input.toCharArray().length; i < charArrayLength; i++) {
            char c = input.toCharArray()[i];
            switch (c) {
                case '{' -> {
                    start = i;
                    Return aReturn = parse1(i + 1, input);
                    input = input.substring(0, i) + aReturn.returnValue + input.substring(aReturn.continueIndex);
                    i = aReturn.continueIndex;
//                    System.out.println("returnValue = " + aReturn.returnValue);
                }
                case '}' -> {
                    System.out.println("hook: " + input.substring(start, i));
                    return new Return(i + 1, name);
                }
                default -> name += c;
            }
        }
        return null;
    }

    enum State {
        Name,
        Hook,
        String
    }

    public static String parse2(String input) {
        String name = "";
        String string = "";
        State state = State.Name;
        ArrayList<String> parameter = new ArrayList<>();

        int depth = 0;
        int startParameterIndex = 0;
        for (int i = 0, charArrayLength = input.toCharArray().length; i < charArrayLength; i++) {
            char c = input.toCharArray()[i];
            if (state == State.Name) {
                //Falls wir noch den Namen suchen wird der neue Buchstabe immer an den Namen angehangen
                if (c == '}') {
                    //Falls wir noch den Namen Suchen, aber der Hook bereits Fertig ist, haben wir keine Parameter, also sind wir mit dem Parsen fertig
                    break;
                }
                name += c;
            }

            if (c == '{') {
                //Falls wir auf } stoßen ist der name vorbei und wir suchen beginen ein Hook als Parameter
                state = State.Hook;
                if (depth == 0) {
                    startParameterIndex = i;
                }
                depth++;
            }
            if (state == State.Hook && c == '}') {
                depth--;
                if (depth == 0) {
                    String substring = input.substring(startParameterIndex + 1, i);

//                    parameter.add(substring);
                    parameter.add(parse2(substring));
                }
            }

            if (state == State.String) {
                if (c == '°') {
                    parameter.add(string);
                    string = "";
                }
                string += c;
            }

            if (c == '°')
                //Falls wir auf ° stoßen ist der name vorbei und wir beginnen einen String
                state = State.String;

        }

//        System.out.println("name = " + name);;
//        System.out.println(name + " : " + parameter.toString());
        String parameterS = parameter.toString();
        return name + "(" + parameterS.substring(1, parameterS.length() - 1) + ")";
        //Rufe methode auf
    }

    public static String parse3(String input) {
        input = input.trim();
        System.out.println("input = " + input);

        //Find name
        SmallestControlChar smallestControlChar = findSmallestControllChar(input);
        String name = input.substring(0, smallestControlChar.index);

        System.out.println("name = " + name);

        HashMap<Integer, String> parameter = new HashMap<>();

        //Make Param List
        int depth = 0;
        int currentParamStart = 0;
        int stringstart = 0;

        int index = 0;
        do {
            smallestControlChar = findSmallestControllChar(input);
            switch (smallestControlChar.charType) {
                case OpenBracket -> {
                    if (depth == 0)
                        currentParamStart = smallestControlChar.index;
                    depth++;
                }
                case CloseBracket -> {
                    depth--;
                    if (depth == 0) {
                        currentParamStart = 0;
                        String substring = input.substring(currentParamStart, smallestControlChar.index);
                        String response = parse3(substring);

                        parameter.put(index, response);
                        index++;
                        System.out.println("Found " + substring  + " ->" + response);
                    }
                }
                case Grad -> {
                    if (stringstart == 0) {
                        stringstart = smallestControlChar.index;
                        depth++;
                    } else {
                        parameter.put(index, input.substring(stringstart, smallestControlChar.index));
                        index++;
                        depth--;
                    }
                }
            }
        } while (depth != 0 && input.length() > 0);

        String parameterS = parameter.toString();
        return name + "(" + parameterS.substring(1, parameterS.length() - 1) + ")";
    }

    static class SmallestControlChar {
        enum ControlCharType {
            CloseBracket,
            OpenBracket,
            Grad
        }

        ControlCharType charType;
        int index;

        public SmallestControlChar(ControlCharType charType, int index) {
            this.charType = charType;
            this.index = index;
        }
    }

    public static SmallestControlChar findSmallestControllChar(String input) {
        int closebracket = input.indexOf("}");
        int openbracket = input.indexOf("{");
        int grad = input.indexOf("°");
        //Wenn distanzen negativ sind rolen wir sie zurück, da die Werte nicht negativ sein dürfen
        if (openbracket < 0)
            openbracket = Integer.MAX_VALUE;
        if (closebracket < 0)
            closebracket = Integer.MAX_VALUE;
        if (grad < 0)
            grad = Integer.MAX_VALUE;


        String name = "";
        //Wenn die geschlossene Klammer am nächsten ist, dann
        if (closebracket < openbracket && closebracket < grad) {
            return new SmallestControlChar(SmallestControlChar.ControlCharType.CloseBracket, closebracket);
        }
        if (openbracket < closebracket && openbracket < grad) {
            return new SmallestControlChar(SmallestControlChar.ControlCharType.OpenBracket, openbracket);
        }
        if (grad < openbracket && grad < closebracket) {
            return new SmallestControlChar(SmallestControlChar.ControlCharType.Grad, grad);
        }
        return null;
    }

    public static String parse4(String input) {
        if (!equalBracketNumber(input))
            return "ERROR Alle geöffneten Klammern müssen wieder geschlossen werden!";

        String output = "";

        int startHookIndex = input.indexOf('{');
        while (startHookIndex > 0) {
            //Add part bevor Hook start to output without modifying it
            output += input.substring(0, startHookIndex);

            int endHookIndex = findClosingBracket(input);
            String substring = input.substring(startHookIndex, endHookIndex+1);
            //Parse the substring recursive
            output += parseHooks(substring);
//            output += substring;

            //Delete already parsed part from Input
            input = input.substring(endHookIndex+1);
            startHookIndex = input.indexOf('{');
        }
        return output;
    }

    public static String parseHooks(String hook) {
        //Skip first {
        hook = hook.substring(1);
        String name = "";
        HashMap<Integer, String> parameter = new HashMap<>();

        //Does not have Parameter
        if (!hook.contains("{") && !hook.contains("°")) {
            name = hook.substring(0, hook.length()-1);
            //Execute hook
            return name;
        }


        //Has a parameter
        name = textTillControlChar(hook);
        //Remove name from the hook String
        hook = hook.replace(name, "");

        int index = 0;
        //Loop
        while (hook.length() > 0) {
            switch (hook.charAt(0)) {
                //Parameter is Hook
                case '{' -> {
                    //Find Part of the String that represents the Hook that is the Parameter
                    int endIndex = findClosingBracket(hook);
                    String substring = hook.substring(0, endIndex+1);
//                    System.out.println("substring = " + substring);

                    //Parse the Hook recursive
                    String returnString = parseHooks(substring);

                    //Add to Parameter Map
                    parameter.put(index, returnString);
                    index++;

                    //Remove parsed part from String
                    hook = hook.replace(substring, "");
                }
                //Parameter is String
                case '°' -> {
                    int endIndex = hook.substring(1).indexOf("°")+1;
                    String substring = hook.substring(0, endIndex+1);

                    //Add to Parameter Map
                    parameter.put(index, substring);
                    index++;

                    //Remove parsed part from String
                    hook = hook.replace(substring, "");
                }
                case '}', ' ' -> hook = hook.substring(1);
                //                default -> hook = hook.substring(1);
            }

        }
        //Execute hook
//        System.out.println(name + " := " + parameter.toString());

        String parameterS = parameter.toString();
        return name + "( " + parameterS.substring(1, parameterS.length() - 1) + ")";
    }

    public static String textTillControlChar(String input) {
        Regex regex = new Regex("^[^{}°]*");
        MatchResult result = regex.find(input, 0);
        assert result != null;
        return result.getValue();
    }
    public static int indexNextControlChar(String input) {
        
        int closebracket = input.indexOf("}");
        int openbracket = input.indexOf("{");
        int grad = input.indexOf("°");
        //Wenn distanzen negativ sind rolen wir sie zurück, da die Werte nicht negativ sein dürfen
        if (openbracket < 0)
            openbracket = Integer.MAX_VALUE;
        if (closebracket < 0)
            closebracket = Integer.MAX_VALUE;
        if (grad < 0)
            grad = Integer.MAX_VALUE;


        String name = "";
        //Wenn die geschlossene Klammer am nächsten ist, dann
        if (closebracket < openbracket && closebracket < grad) {
            return closebracket;
        }
        if (openbracket < closebracket && openbracket < grad) {
            return openbracket;
        }
        if (grad < openbracket && grad < closebracket) {
            return grad;
        }
        return 0;
    }

    /**
     * Nimmt zb. so einen Input:
     * <br> {@code  {ifequals {messageSource} °User° {atUser} } restlicher COmmand, hallo!}
     * und returned den Index der schließenden Klammer
     * @param input
     * @return
     */
    public static int findClosingBracket(String input) {
        int start = input.indexOf('{')+1;
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

    public static boolean equalBracketNumber(String input) {
        int open = 0;
        int close = 0;

        for (char aChar:input.toCharArray()) {
            if (aChar == '{') {
                open++;
            } else if (aChar == '}') {
                close++;
            }
        }
        return open == close;
    }
}
