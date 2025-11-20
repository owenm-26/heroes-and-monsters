package UI;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static UI.CommandType.getControlInstructions;

public class UserInputs {
    final private static List<CommandType> MOVEMENT_COMMANDS = Arrays.asList(new CommandType[]{CommandType.UP, CommandType.DOWN, CommandType.LEFT, CommandType.RIGHT});
    public static final Scanner scanner = new Scanner(System.in);


    public static boolean isCommand(String input, CommandType command){
        String s = prepInput(input);
        return s.equals(command.getCode());
    }

    public static boolean isMovement(String input, CommandType command){
        String s = prepInput(input);
        return MOVEMENT_COMMANDS.contains(command);
    }

    private static String prepInput(String input){
        return input.toLowerCase().trim();
    }

    public static String parseAndQuitIfAsked(){
        String input = scanner.next();
        terminateIfRequested(input);
        return prepInput(input);
    }

    private static void terminateIfRequested(String input){
        if (isCommand(input, CommandType.QUIT)) {
            ConsoleColors.printInColor(ConsoleColors.RED_BOLD, "User requested quit. Terminating.");
            System.exit(0);
        }
        if(isCommand(input, CommandType.HELP)){
            getControlInstructions();
        }
    }

    public static int showMenuAndGetUserAnswer(String[] options) {
        /*
        Reusable way to generate menus and get user answers easier
         */

        while (true) {
            int i =0;
            System.out.println("Choose an Option:");
            while (i < options.length){
                System.out.format("[%d] %s", (i+1), options[i]);
                System.out.println();
                i++;
            }
            System.out.print("Enter choice: ");
            String input = UserInputs.parseAndQuitIfAsked();

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                if (!input.equals(CommandType.HELP.getCode()))System.out.println("Invalid choice.\n");

                continue;
            }
            return choice-1;
        }
    }

}
