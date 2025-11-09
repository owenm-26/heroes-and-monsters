package UI;

import java.util.Arrays;
import java.util.List;

public class UserInputs {
    final private static List<CommandType> MOVEMENT_COMMANDS = Arrays.asList(new CommandType[]{CommandType.UP, CommandType.DOWN, CommandType.LEFT, CommandType.RIGHT});

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

}
