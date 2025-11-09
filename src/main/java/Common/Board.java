package Common;

import GameBoard.HMSquare;
import GameBoard.SquareType;
import UI.ConsoleColors;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.util.HashMap;
import java.util.Random;

public abstract class Board<T extends Square> {

    final static Random random = new Random();
    protected HMSquare[][] grid;

    public Board(int n) {
        validateDimension(n);
    }

    public Board() {
        this(6);
    }

    protected abstract void validateDimension(int n);


    public abstract void displayBoard();
}