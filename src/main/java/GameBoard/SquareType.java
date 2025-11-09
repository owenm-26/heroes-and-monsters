package GameBoard;

import UI.ConsoleColors;

public enum SquareType {

    COMMON(" ", ConsoleColors.GREEN),
    BLOCKED("X", ConsoleColors.RED),
    MARKET("M", ConsoleColors.CYAN);
    private String symbol;
    private String color;
    SquareType(String symbol, String color) {
        this.symbol = symbol;
        this.color = color;
    }

    public String getSymbol(){
        return this.symbol;
    }

    public String getColor() {
        return color;
    }
}
