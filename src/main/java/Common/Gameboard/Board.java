package Common.Gameboard;

import HeroesAndMonsters.GameBoard.HMSquare.HMSquare;
import Utility.UI.CommandType;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public abstract class Board<T extends Square> {

    final static protected Map<String, Integer[]> dirs = new HashMap<>();

    protected T[][] grid;

    static {
        // populate directions
        dirs.put(CommandType.UP.getCode(), new Integer[]{-1, 0});
        dirs.put(CommandType.DOWN.getCode(), new Integer[]{1, 0});
        dirs.put(CommandType.LEFT.getCode(), new Integer[]{0, -1});
        dirs.put(CommandType.RIGHT.getCode(), new Integer[]{0, 1});
    }

    final static protected Random random = new Random();

    public Board(int n) {
        validateDimension(n);
    }

    public Board() {
        this(6);
    }

    protected abstract void validateDimension(int n);


    public abstract void displayBoard();
}