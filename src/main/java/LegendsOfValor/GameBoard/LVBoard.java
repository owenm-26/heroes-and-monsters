package LegendsOfValor.GameBoard;

import Common.Gameboard.Board;
import LegendsOfValor.GameBoard.LVSquare.LVSquare;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

public class LVBoard extends Board<LVSquare> {
    @Override
    protected void validateDimension(int n) {
        if (n != 8) throw new ValueException("Must be an 8 x 8 board");
    }

    @Override
    public void displayBoard() {

    }
}
