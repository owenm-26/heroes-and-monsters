package HeroesAndMonsters.GameBoard;

import Common.Figures.Figure;
import Common.Figures.Party;
import Common.Gameboard.Board;
import Common.Items.Inventory;
import HeroesAndMonsters.GameBoard.HMSquare.HMSquare;
import HeroesAndMonsters.GameBoard.HMSquare.HMSquareType;
import Utility.UI.CommandType;
import Utility.UI.UserInputs;


import java.util.*;

import static HeroesAndMonsters.GameBoard.HMSquare.HMSquare.battleProbability;
import static Utility.UI.GeneralPrints.padCenter;
import static Utility.Validators.Integers.integerInInclusiveRange;

public class HMBoard extends Board<HMSquare> {

    private int[] heroPartyCoordinates= new int[2];
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
        heroPartyCoordinates[0] = n-1;
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
                    if(type == HMSquareType.MARKET) s.setInventory(Inventory.generateMarketInventory());
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
            throw new IllegalArgumentException(String.format("Provided Dimension (%d) is out of the valid range 3-15", n));
        }
    }

    private boolean partyCanMakeRequestedMovement(int[] prospectiveCoordinates){
        /*
        Returns whether the requested move by user is valid (in bounds and not on blocked square
         */
        return integerInInclusiveRange(prospectiveCoordinates[0], 0, grid.length-1) &&
                integerInInclusiveRange(prospectiveCoordinates[1], 0, grid.length-1) &&
                grid[prospectiveCoordinates[0]][prospectiveCoordinates[1]].getType() != HMSquareType.BLOCKED;
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

    public void handleMovement(String input, HMGame g){
        if(!UserInputs.isMovement(input)) throw new IllegalArgumentException(String.format("%s is not a movement command", input));
        int[] prospectiveCoordinates = {heroPartyCoordinates[0] + dirs.get(input)[0], heroPartyCoordinates[1] + dirs.get(input)[1]};
        if (!partyCanMakeRequestedMovement(prospectiveCoordinates)) {
            System.out.println("⚠️ Move invalid.");
            return;
        }
        //unset prev square
        Party<Figure> p = grid[heroPartyCoordinates[0]][heroPartyCoordinates[1]].getPartyOnSquare();
        grid[heroPartyCoordinates[0]][heroPartyCoordinates[1]].setPartyOnSquare(null);
        //set new square
        grid[prospectiveCoordinates[0]][prospectiveCoordinates[1]].setPartyOnSquare(p);
        heroPartyCoordinates = prospectiveCoordinates;

        //random chance of battling on common space
        if (grid[heroPartyCoordinates[0]][heroPartyCoordinates[1]].getType() == HMSquareType.COMMON && Math.random() < battleProbability){
            g.setState(HMGameState.BATTLING);
        }
    }

    public boolean partyIsInMarket(){
        /*
        Returns whether the party is on a market square to determine if they can enter it
         */

        return grid[heroPartyCoordinates[0]][heroPartyCoordinates[1]].getType() == HMSquareType.MARKET;
    }

    public Inventory getMarketInventory(){
        /*
        Returns the inventory of the space that the players are on
         */
        if (!partyIsInMarket()) throw new IllegalArgumentException("This square is not a market");
        return grid[heroPartyCoordinates[0]][heroPartyCoordinates[1]].getInventory();
    }

}
