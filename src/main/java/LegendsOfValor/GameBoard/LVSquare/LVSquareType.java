package LegendsOfValor.GameBoard.LVSquare;

import Common.Figures.TraitType;
import Utility.UI.ConsoleColors;

public enum LVSquareType {
    PLAIN(" ", ConsoleColors.WHITE_BACKGROUND, null),
    INACCESSIBLE("X", ConsoleColors.BLACK_BACKGROUND, null),
    NEXUS(" ", ConsoleColors.YELLOW_BACKGROUND, null),

    OBSTACLE("O", ConsoleColors.RED_BACKGROUND, null),

    BUSH(" ", ConsoleColors.GREEN_BACKGROUND, TraitType.DEXTERITY),

    CAVE(" ", ConsoleColors.CYAN_BACKGROUND, TraitType.AGILITY),

    KOULOU(" ", ConsoleColors.PURPLE_BACKGROUND, TraitType.STRENGTH)
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

    public static void displayLegend(){
        StringBuilder s = new StringBuilder();
        s.append("-- BOARD LEGEND --\n");
        for (LVSquareType t: LVSquareType.values()) {
            String body = String.format(" %s =  %s",t.symbol, t.name());
            String bonus = t.bonusGiven != null ? String.format("[++%s]", t.bonusGiven) : "";
            String line = String.format("%s %s", body, bonus);
            s.append(ConsoleColors.returnInColor(t.color, line, true));
        }

        System.out.println(s);
    }
}

