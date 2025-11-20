package GameBoard;

import Common.Piece;
import Common.Square;
import Figures.Figure;
import Figures.Party;
import Items.Inventory;
import UI.ConsoleColors;

import java.util.List;

import static UI.ConsoleColors.BACKGROUND_MAP;

public class HMSquare extends Square<SquareType> {

    public final static float battleProbability = 0.2f;
    private Inventory inventory;
    private Party<Figure> partyOnSquare;

    public HMSquare(SquareType type){
        super(type);

        if (type == SquareType.MARKET){
            inventory = Inventory.generateMarketInventory();
        }
    }

    public HMSquare(){
        this(SquareType.COMMON);
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
