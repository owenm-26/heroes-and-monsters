package LegendsOfValor.GameBoard.LVSquare;

import Common.Gameboard.Square;
import Common.Items.Inventory;
import Utility.UI.ConsoleColors;

public class LVSquare extends Square<LVSquareType> {

    private Inventory inventory;

    public LVSquare(LVSquareType type){
        super(type);
        if (type == LVSquareType.NEXUS){
            inventory = Inventory.generateMarketInventory();
        }
    }

    @Override
    public void printSymbol() {
        System.out.print(toString());
    }

    @Override
    public String toString() {
        String color = type.getColor();
        String symbol = type.getSymbol();
        return color + symbol + ConsoleColors.RESET;
    }

    public Inventory getInventory() {
//        if(type != LVSquareType.NEXUS)
//            throw new IllegalArgumentException(
//                    String.format("Only Nexuses have inventories. You called getInventory() on a %s space", type.name())
//            );
        return inventory;
    }
}
