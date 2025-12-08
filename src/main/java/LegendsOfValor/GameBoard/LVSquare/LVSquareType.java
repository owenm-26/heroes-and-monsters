package LegendsOfValor.GameBoard.LVSquare;

import Common.Figures.TraitType;
import Utility.UI.ConsoleColors;

public enum LVSquareType {

    PLAIN(" ", ConsoleColors.WHITE_BACKGROUND, null),
    INACCESSIBLE("X", ConsoleColors.RED, null),
    NEXUS("M", ConsoleColors.CYAN, null),

    OBSTACLE("O", ConsoleColors.BLUE, null),

    BUSH("B", ConsoleColors.GREEN, TraitType.DEXTERITY),

    CAVE("C", ConsoleColors.BLACK_BACKGROUND, TraitType.AGILITY),

    KOULOU("K", ConsoleColors.YELLOW, TraitType.STRENGTH)
    ;
    private String symbol;
    private String color;

    private TraitType bonusGiven;
    LVSquareType(String symbol, String color, TraitType bonusGiven) {
        this.symbol = symbol;
        this.color = color;
    }

    public String getSymbol(){
        return this.symbol;
    }

    public String getColor() {
        return color;
    }

    public TraitType getBonusGiven(){return bonusGiven;}
}

