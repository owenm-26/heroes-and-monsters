package GameBoard;

import Items.Inventory;

public class Square {

    final static float battleProbability = 0.2f;

    private SquareType type;
    private Inventory inventory;

    public Square(SquareType type){
        this.type = type;

        if (type == SquareType.MARKET){
            inventory = Inventory.generateMarketInventory();
        }
    }

    public Square(){
        this(SquareType.COMMON);
    }
}
