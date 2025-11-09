package GameBoard;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class Board {

    final static Random random = new Random();
    final static HashMap<SquareType, Float> squareMakeup = new HashMap<>();
    final static SquareType[] squareChoiceArray = new SquareType[10];

    static{
        // Make square array to randomly choose from
        squareMakeup.put(SquareType.COMMON, 0.5f);
        squareMakeup.put(SquareType.MARKET, 0.3f);
        squareMakeup.put(SquareType.BLOCKED, 0.2f);

        int i=0;

        for(SquareType key: squareMakeup.keySet()){
            int j = (int)(squareMakeup.get(key) * 10);
            while (j > 0 && i < squareChoiceArray.length){
                squareChoiceArray[i] = key;
                j--;
                i++;
            }
        }
    }



    private Square[][] grid;
    public Board(int n){
        validateDimension(n);
        grid = new Square[n][n];

        // Randomly create different types of Squares
        for(int r =0; r < n; r++){
            for(int c=0; c<n; c++){
                SquareType type = squareChoiceArray[random.nextInt(squareChoiceArray.length)];
                grid[r][c] = new Square(type);
            }
        }
    }

    public Board(){
        this(6);
    }

    public void validateDimension(int n){
        if (n  < 3 || n > 15){
            throw new ValueException(String.format("Provided Dimension (%d) is out of the valid range 3-15", n));
        }
    }
}
