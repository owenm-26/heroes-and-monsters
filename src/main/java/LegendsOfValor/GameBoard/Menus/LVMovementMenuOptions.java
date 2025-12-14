package LegendsOfValor.GameBoard.Menus;

public enum LVMovementMenuOptions {
    MOVE("Move Character"),
    TELEPORT("Teleport"),
    RECALL("Recall")
    ;

    private String code;

    LVMovementMenuOptions(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static LVMovementMenuOptions fromName(String input){
        for (LVMovementMenuOptions a : values()) {
            if (a.code.equalsIgnoreCase(input))
                return a;
        }
        throw new IllegalArgumentException("Invalid LVMovementMenuOptions: " + input);
    }
}
