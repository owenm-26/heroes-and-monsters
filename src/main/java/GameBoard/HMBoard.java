package GameBoard;

import Common.Board;
import UI.ConsoleColors;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.util.HashMap;
import java.util.Random;

public class HMBoard extends Board<HMSquare> {

    final static Random random = new Random();
    final static HashMap<SquareType, Float> squareMakeup = new HashMap<>();
    final static SquareType[] squareChoiceArray = new SquareType[10];

    static{
        // Make square array to randomly choose from
        squareMakeup.put(SquareType.COMMON, 0.7f);
        squareMakeup.put(SquareType.MARKET, 0.1f);
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

    private HMSquare[][] grid;
    public HMBoard(int n){
        validateDimension(n);
        grid = new HMSquare[n][n];

        // Randomly create different types of Squares
        for(int r =0; r < n; r++){
            for(int c=0; c<n; c++){
                SquareType type = squareChoiceArray[random.nextInt(squareChoiceArray.length)];
                grid[r][c] = new HMSquare(type);
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

    public void displayBoard(){
        int n = this.grid.length;
        // find how big each square should
        int cellWidth = Integer.toString((n*n) -1).length();
        StringBuilder topBuilder = new StringBuilder();
        for(int l=0;l<cellWidth+2; l++){
            topBuilder.append("-");
        }
        StringBuilder horizontalBuilder = new StringBuilder();
        horizontalBuilder.append("+");
        for (int c = 0; c < n; c++) {
            for (int i = 0; i < cellWidth + 2; i++) {
                horizontalBuilder.append("-");
            }
            horizontalBuilder.append("+");
        }
        String horizontal = horizontalBuilder.toString();

        for(int row=0; row< n; row++){
            System.out.println(horizontal); // print a horizontal above
            System.out.print("|"); // The leftmost vertical bar
            for(int col=0; col < n; col++){
                SquareType t = grid[row][col].getType();
                String content = t.getSymbol();
                String formatted = String.format(" %" + cellWidth + "s ", content);
                ConsoleColors.printInColor(t.getColor(), formatted, false);
                System.out.print("|");
            }
            System.out.println(); // start new line
        }
        System.out.println(horizontal); // the bottom horizontal
    }
}
