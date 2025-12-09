package HeroesAndMonsters.GameBoard.HMSquare;

import Common.Figures.Figure;
import Common.Figures.Party;
import Common.Gameboard.Square;
import Common.Items.Inventory;
import Utility.UI.ConsoleColors;

public class HMSquare extends Square<HMSquareType> {

    public final static float battleProbability = 0.2f;
    private Inventory inventory;
    private Party<Figure> partyOnSquare;

    public HMSquare(HMSquareType type){
        super(type);

        if (type == HMSquareType.MARKET){
            inventory = Inventory.generateMarketInventory();
        }
    }

    public HMSquare(){
        this(HMSquareType.COMMON);
    }

    public void printSymbol(){
        System.out.println(returnSymbol());
    }

    public String returnSymbol() {
        if (partyOnSquare != null) {
            return ConsoleColors.returnInColor(
                    type.getColor(),
                    type.getSymbol(),
                    false
            ) + ConsoleColors.returnInColor(
                    ConsoleColors.PURPLE_BOLD,
                    "P",
                    false
            );
        }
        return ConsoleColors.returnInColor(type.getColor(), type.getSymbol(), false);
    }


    public Inventory getInventory() {
        return inventory;
    }

    public Party<Figure> getPartyOnSquare() {
        return partyOnSquare;
    }

    public void setPartyOnSquare(Party<Figure> partyOnSquare) {
        this.partyOnSquare = partyOnSquare;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
