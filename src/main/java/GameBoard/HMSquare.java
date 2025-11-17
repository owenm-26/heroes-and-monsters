package GameBoard;

import Common.Piece;
import Common.Square;
import Items.Inventory;
import UI.ConsoleColors;

import java.util.List;

public class HMSquare extends Square<SquareType> {

    public final static float battleProbability = 0.2f;
    private Inventory inventory;

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
        ConsoleColors.printInColor(type.getColor(), type.getSymbol(), false);
    }

    public Inventory getInventory() {
        return inventory;
    }

}
