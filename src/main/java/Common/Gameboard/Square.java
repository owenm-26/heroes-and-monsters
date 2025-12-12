package Common.Gameboard;

import java.util.ArrayList;
import java.util.List;

public abstract class Square<T> {
    protected T type;
    protected List<Piece> pieces;

    public Square(T type){
        this.type = type;
        // FIX: initialize pieces list
        this.pieces = new ArrayList<>();
    }

    public abstract void printSymbol();

    public T getType() {
        return type;
    }

    public List<Piece> getPieces() {
        return pieces;
    }
}
