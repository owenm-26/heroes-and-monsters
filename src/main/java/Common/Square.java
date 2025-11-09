package Common;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

public abstract class Square<T> {
    protected T type;

    public Square(T type){
        this.type = type;
    }

    public abstract void printSymbol();

    public T getType() {
        return type;
    }
}

