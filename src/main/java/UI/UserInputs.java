package UI;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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
    }

}
