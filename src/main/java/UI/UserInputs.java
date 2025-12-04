package UI;

import GameBoard.HMGame;
import GameBoard.HMGameState;

import java.util.*;

import static UI.CommandType.getControlInstructions;
import static UI.CommandType.validCommandStrings;

public class UserInputs {
    final private static List<String> MOVEMENT_COMMANDS = Arrays.asList(new String[]{CommandType.UP.getCode(), CommandType.DOWN.getCode(), CommandType.LEFT.getCode(), CommandType.RIGHT.getCode()});
    public static final Scanner scanner = new Scanner(System.in);

    public static boolean isAnyCommand(String input) {
        /*
        Returns whether the user input is a valid command
         */
        return validCommandStrings.contains(input);
    }

    public static boolean isExploringCommand(String input) {
        /*
        Returns whether this is a valid command for the exploring state
         */
        return isAnyCommand(input) && (isMovement(input) || isHelpOrQuitOrInfo(input) || isCommand(input, CommandType.MARKET) || isCommand(input, CommandType.BACKPACK));
    }

    public static boolean isCommand(String input, CommandType command) {
        String s = prepInput(input);
        return s.equals(command.getCode());
    }

    public static boolean isMovement(String input) {
        String s = prepInput(input);
        return MOVEMENT_COMMANDS.contains(s);
    }

    private static String prepInput(String input) {
        return input.toLowerCase().trim();
    }

    public static String parseAndQuitIfAsked() {
        String input = scanner.next();
        terminateIfRequested(input);
        return prepInput(input);
    }

    public static String toggleInventoryParseAndQuitIfAsked(HMGame g) {
        String parsed = parseAndQuitIfAsked();
        if (isCommand(parsed, CommandType.INFO)) g.toggleStatistics();
        return parsed;
    }

    private static void terminateIfRequested(String input) {
        if (isCommand(input, CommandType.QUIT)) {
            ConsoleColors.printInColor(ConsoleColors.RED_BOLD, "User requested quit. Terminating.");
            System.exit(0);
        }
        if (isCommand(input, CommandType.HELP)) {
            getControlInstructions();
        }
    }

    private static boolean isHelpOrQuitOrInfo(String input) {
        return isCommand(input, CommandType.HELP) || isCommand(input, CommandType.QUIT) || isCommand(input, CommandType.INFO);
    }

    public static int showMenuAndGetUserAnswer(String[] options, boolean toggleMenuAbility, HMGame g) {
        /*
        Reusable way to generate menus and get user answers easier
         */

        while (true) {
            int i = 0;
            System.out.println("Choose an Option:");
            while (i < options.length && options[i] != null) {
                System.out.format("[%d] %s", (i + 1), options[i]);
                System.out.println();
                i++;
            }
            System.out.print("Enter choice ('q' to quit game, '0' to go back");
            String input;
            if (g!=null && g.getState() == HMGameState.EXPLORING) System.out.print(", 'b' to open backpack");
            if (toggleMenuAbility) {
                if (g == null) throw new IllegalArgumentException("Toggle Menu set but game is null");

                System.out.print(", 'i' to get character statistics): ");
                input = UserInputs.toggleInventoryParseAndQuitIfAsked(g);
                if (isCommand(input, CommandType.INFO)) return -1;
            } else {
                System.out.print("): ");
                input = UserInputs.parseAndQuitIfAsked();
            }


            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                if (!input.equals(CommandType.HELP.getCode())) System.out.println("Invalid choice.\n");

                continue;
            }

            if (choice > options.length){
                System.out.println("Invalid Choice. Out of bounds. Try again\n");
                continue;
            }
            return choice - 1;
        }
    }

    public static int showMenuAndGetUserAnswer(String[] options) {
        return showMenuAndGetUserAnswer(options, false, null);
    }
}
