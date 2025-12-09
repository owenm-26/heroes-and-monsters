package LegendsOfValor.GameBoard.LVSquare;

import Common.Gameboard.Square;
import Common.Items.Inventory;

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

    }

    // GETTERS
    public Inventory getInventory() {
        if(type != LVSquareType.NEXUS) throw new IllegalArgumentException(String.format("Only Nexuses have inventories. You called getInventory() on a %s space", type.name()));
        return inventory;
    }
}
