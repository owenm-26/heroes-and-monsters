package LegendsOfValor.GameBoard.Menus;

public enum LVMainMenuOptions {
    MOVE("Move"),
    BACKPACK("Open Backpack"),
    STATS("See Statistics"),

    MARKET("Enter Market"),

    ATTACK("Attack"),

    REMOVE_OBSTACLE("Remove Obstacle"),
    PASS("Pass");
    private String code;
    LVMainMenuOptions(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static LVMainMenuOptions fromName(String input){
        for (LVMainMenuOptions a : values()) {
            if (a.code.equalsIgnoreCase(input))
                return a;
        }
        throw new IllegalArgumentException("Invalid LVMenuOptions: " + input);
    }
}
