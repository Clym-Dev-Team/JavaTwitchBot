package main.system;

public class ASCIIProgressbar {
    private static final String style0 = "[####------]";
    private static final String style1 = "[####======]";
    private static final String style2 = "[■■■■------]";
    private static final String style3 = "[||||      ]";
    private static final String style4 = "[※※※※......]";
    private static final String style5 = "[⁜⁜⁜⁜......]";
    private static final String style6 = "[■■■■▨▨▨▨▨▨]";
    private static final String style7 = "[▦▦▦▦▭▭▭▭▭▭]";


    public static String asciiBar(int value, int maxValue, int length) {
        return asciiBar(value, maxValue, length, "■", "-", "[", "]", true, true);
    }

    public static String asciiBar(int value, int maxValue, int length, String completed, String placeHolder, String start, String end, boolean withPercent, boolean withAmount) {

        double percent;
        if (maxValue != 0)
            percent = value/maxValue;
        else
            percent = 1.D;
        int amountPos = (int) Math.round(length * percent);
        int amountRest = length - amountPos;

        String output = start + completed.repeat(amountPos) + placeHolder.repeat(amountRest) + end;
        if (withPercent)
            output += percent*100 + "% ";
        if (withAmount)
            output += value + "/" + maxValue;
        return  output;
    }
}
