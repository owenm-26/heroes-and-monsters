package GameBoard;

import Common.Board;
import Figures.Party;
import GameBoard.HMSquare.HMSquare;
import GameBoard.HMSquare.HMSquareType;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import static UI.GeneralPrints.padCenter;

public class HMBoard extends Board<HMSquare> {

    private HMSquare[][] grid;
    private int[] heroPartyCoordinates= new int[2];
    final static Random random = new Random();
    final static HashMap<HMSquareType, Float> squareMakeup = new HashMap<>();
    final static HMSquareType[] squareChoiceArray = new HMSquareType[10];

    static{
        // Make square array to randomly choose from
        squareMakeup.put(HMSquareType.COMMON, 0.7f);
        squareMakeup.put(HMSquareType.MARKET, 0.1f);
        squareMakeup.put(HMSquareType.BLOCKED, 0.2f);

        int i=0;
        for(HMSquareType key: squareMakeup.keySet()){
            int j = (int)(squareMakeup.get(key) * 10);
            while (j > 0 && i < squareChoiceArray.length){
                squareChoiceArray[i] = key;
                j--;
                i++;
            }
        }
    }


    public HMBoard(int n){
        validateDimension(n);
        grid = new HMSquare[n][n];
        heroPartyCoordinates[0] = 0;
        heroPartyCoordinates[1]= n / 2;

        // Randomly create different types of Squares
        for(int r =0; r < n; r++){
            for(int c=0; c<n; c++){
                HMSquareType type = squareChoiceArray[random.nextInt(squareChoiceArray.length)];
                HMSquare s;
                if (r==heroPartyCoordinates[0] && c==heroPartyCoordinates[1]) {
                    s = new HMSquare(HMSquareType.COMMON);
                    s.setPartyOnSquare(new Party<>(0));// placeholder party for visuals
                }
                else{
                    s = new HMSquare(type);
                }


                grid[r][c] = s;
            }
        }
    }

    public HMBoard(){
        this(6);
    }

    protected void validateDimension(int n){
        if (n  < 3 || n > 15){
            throw new ValueException(String.format("Provided Dimension (%d) is out of the valid range 3-15", n));
        }
    }


    public void displayBoard() {
        int n = grid.length;
        int cellWidth = 5; // fixed width for each cell

        // build horizontal border
        String border = "+";
        for (int c = 0; c < n; c++) {
            border += String.join("", Collections.nCopies(cellWidth, "-")) + "+";
        }

        for (int row = 0; row < n; row++) {
            System.out.println(border);
            System.out.print("|");

            for (int col = 0; col < n; col++) {
                String symbol = grid[row][col].returnSymbol();
                String padded = padCenter(symbol, cellWidth);
                System.out.print(padded + "|");
            }

            System.out.println();
        }

        System.out.println(border);
    }

}
