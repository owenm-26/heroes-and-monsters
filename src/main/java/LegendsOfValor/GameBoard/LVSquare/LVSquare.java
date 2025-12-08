package LegendsOfValor.GameBoard.LVSquare;

import Common.Gameboard.Square;
import Common.Items.Inventory;
import HeroesAndMonsters.GameBoard.HMSquare.HMSquareType;

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
}
