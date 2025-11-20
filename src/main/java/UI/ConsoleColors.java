package UI;

// Source - https://stackoverflow.com/a
// Posted by shakram02

import java.util.HashMap;
import java.util.Map;

public class ConsoleColors {

    public static void printInColor(String color, String message, boolean newLine){
        System.out.print(returnInColor(color, message, false));
        if (newLine) System.out.println();
    }

    public static String returnInColor(String color, String message, boolean newLine){
        StringBuilder b = new StringBuilder();
        b.append(color);
        b.append(message);
        b.append(RESET);

        if (newLine) b.append(NEW_LINE);
        return b.toString();
    }

    public static String returnInColor(String color, String message){
       return returnInColor(color, message, true);
    }



    public static void printInColor(String color, String message){
        printInColor(color, message, true);
    }
    // Reset
    public static final String RESET = "\033[0m";  // Text Reset
    public static final String NEW_LINE = "\n";
    // Regular Colors
    public static final String BLACK = "\033[0;30m";   // BLACK
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String PURPLE = "\033[0;35m";  // PURPLE
    public static final String CYAN = "\033[0;36m";    // CYAN
    public static final String WHITE = "\033[0;37m";   // WHITE

    // Bold
    public static final String BLACK_BOLD = "\033[1;30m";  // BLACK
    public static final String RED_BOLD = "\033[1;31m";    // RED
    public static final String GREEN_BOLD = "\033[1;32m";  // GREEN
    public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
    public static final String BLUE_BOLD = "\033[1;34m";   // BLUE
    public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
    public static final String CYAN_BOLD = "\033[1;36m";   // CYAN
    public static final String WHITE_BOLD = "\033[1;37m";  // WHITE

    // Background
    public static final String BLACK_BACKGROUND = "\033[40m";  // BLACK
    public static final String RED_BACKGROUND = "\033[41m";    // RED
    public static final String GREEN_BACKGROUND = "\033[42m";  // GREEN
    public static final String YELLOW_BACKGROUND = "\033[43m"; // YELLOW
    public static final String BLUE_BACKGROUND = "\033[44m";   // BLUE
    public static final String PURPLE_BACKGROUND = "\033[45m"; // PURPLE
    public static final String CYAN_BACKGROUND = "\033[46m";   // CYAN
    public static final String WHITE_BACKGROUND = "\033[47m";  // WHITE

    public static final Map<String, String> BACKGROUND_MAP = new HashMap<>();
    static {
        BACKGROUND_MAP.put(BLACK, BLACK_BACKGROUND);
        BACKGROUND_MAP.put(RED, RED_BACKGROUND);
        BACKGROUND_MAP.put(GREEN, GREEN_BACKGROUND);
        BACKGROUND_MAP.put(YELLOW, YELLOW_BACKGROUND);
        BACKGROUND_MAP.put(BLUE, BLUE_BACKGROUND);
        BACKGROUND_MAP.put(PURPLE, PURPLE_BACKGROUND);
        BACKGROUND_MAP.put(CYAN, CYAN_BACKGROUND);
        BACKGROUND_MAP.put(WHITE, WHITE_BACKGROUND);
    }

}



