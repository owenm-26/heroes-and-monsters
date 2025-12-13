package LegendsOfValor.GameBoard.LVSquare;

import Common.Figures.TraitType;
import Utility.UI.ConsoleColors;

public enum LVSquareType {
    //TODO: Change all spaces to have background colors and no letters

    PLAIN(" ", ConsoleColors.WHITE_BACKGROUND, null),
    INACCESSIBLE("X", ConsoleColors.BLACK_BACKGROUND, null),
    NEXUS("M", ConsoleColors.CYAN, null),

    OBSTACLE("O", ConsoleColors.BLUE, null),

    BUSH("B", ConsoleColors.GREEN, TraitType.DEXTERITY),

    CAVE("C", ConsoleColors.RED, TraitType.AGILITY),

    KOULOU("K", ConsoleColors.YELLOW, TraitType.STRENGTH)
    ;
    private String symbol;
    private String color;

    private TraitType bonusGiven;
    LVSquareType(String symbol, String color, TraitType bonusGiven) {
        this.symbol = symbol;
        this.color = color;
        this.bonusGiven = bonusGiven;
    }

    public String getSymbol(){
        return this.symbol;
    }

    public String getColor() {
        return color;
    }

    public TraitType getBonusGiven(){return bonusGiven;}
}

