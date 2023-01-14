package hooks.hooks;

import hooks.Hook;
import standard.repositories.Message;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class one {
    @Hook()
    public static String Math(String operation, String value1, String value2) {
        int int1;
        int int2;
        try {
            int1 = Integer.parseInt(value1);
            int2 = Integer.parseInt(value2);
        } catch (NumberFormatException e) {
            return e.getMessage();
        }
//        System.out.print("Math: " + operation + " " + " v1: " + int1 + " v2: " + int2);

        String returnValue = "";
        switch (operation) {
            case "+" -> returnValue =String.valueOf(int1 + int2);
            case "-" -> returnValue =String.valueOf(int1 - int2);
            case "*" -> returnValue =String.valueOf(int1 * int2);
            case "/" -> returnValue =String.valueOf(int1 / int2);
            case "%" -> returnValue =String.valueOf(int1 % int2);
            case "^" -> returnValue =String.valueOf(Math.pow(int1, int2));
            default -> returnValue ="0";
        }
//        System.out.println(" -> " + returnValue);
        return returnValue;
    }

    @Hook()
    public static String randomInt() {
        return String.valueOf((int) (Math.random()*1000));
    }

    @Hook()
    public static String sender(Message message) {
        if (message.user().isEmpty())
            return "-SYSTEM-";
        return message.user().get().name();
    }

    @Hook()
    public static String senderSubMonts(Message message) {
        if (message.user().isEmpty())
            return String.valueOf(Integer.MAX_VALUE);
        return String.valueOf(message.user().get().subscriberMonths());
    }

    @Hook()
    public static String currentTime(Message message) {
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return myDateObj.format(myFormatObj);
    }

}