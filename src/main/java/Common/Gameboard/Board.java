package Common.Gameboard;

import java.util.Random;

public abstract class Board<T extends Square> {

    final static Random random = new Random();
    protected Square[][] grid;

    public Board(int n) {
        validateDimension(n);
    }

    public Board() {
        this(6);
    }

    protected abstract void validateDimension(int n);


    public abstract void displayBoard();
}