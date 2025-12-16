package Utility.UI;
/*
FILE HEADER:
General prints saved here for consistency and ease
 */

import java.util.Collections;

public class GeneralPrints {

    public static void printHorizontalLine(){
        System.out.println("--------------------------------");
    }

    public static void printDoubleThickHorizontalLine(){System.out.println("===============================");}

    public static String returnDoubleThickHorizontalLine(){ return "===============================";}

    public static String stripAnsi(String s) {
        return s.replaceAll("\u001B\\[[;\\d]*m", "");
    }

    public static String padCenter(String content, int width) {
        String visible = stripAnsi(content);
        int visibleLength = visible.length();

        int padding = width - visibleLength;
        if (padding <= 0) return content;

        int left = padding / 2;
        int right = padding - left;

        String leftPad = String.join("", Collections.nCopies(left, " "));
        String rightPad = String.join("", Collections.nCopies(right, " "));

        return leftPad + content + rightPad;
    }



}
