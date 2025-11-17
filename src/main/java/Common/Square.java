package Common;

import Items.Inventory;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.util.List;

public abstract class Square<T> {
    protected T type;
    protected List<Piece> pieces;

    public Square(T type){
        this.type = type;
    }

    public abstract void printSymbol();

    public T getType() {
        return type;
    }

    public List<Piece> getPieces() {
        return pieces;
    }
}

