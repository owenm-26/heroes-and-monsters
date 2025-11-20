package UI;

import java.util.HashSet;
import java.util.Set;

public enum CommandType {
    QUIT("q"),
    INFO("i"),
    MARKET("m"),
    HELP("h"),
    UP("w"),
    DOWN("s"),
    LEFT("a"),
    RIGHT("d"),
    YES("y"),
    NO("n");

    public static Set<String> validCommandStrings = new HashSet<>();

    static{
        for (CommandType command: CommandType.values()){
            validCommandStrings.add(command.getCode());
        }
    }


    private String code;
    CommandType(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static void getControlInstructions(){
        System.out.println("\n== Movement Commands ==");
        for(CommandType command: CommandType.values()){
            System.out.format("+  %s - %s\n", command.name(), command.code);
        }
        System.out.println();
    }
}
