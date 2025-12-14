package Utility.UI;

public enum LVMenuOptions {
    MOVE("Move"),
    BACKPACK("Open Backpack"),
    STATS("See Statistics"),

    MARKET("Enter Market"),

    ATTACK("Attack"),

    REMOVE_OBSTACLE("Remove Obstacle");
    private String code;
    LVMenuOptions(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static LVMenuOptions fromName(String input){
        for (LVMenuOptions a : values()) {
            if (a.code.equalsIgnoreCase(input))
                return a;
        }
        throw new IllegalArgumentException("Invalid LVMenuOptions: " + input);
    }
}
