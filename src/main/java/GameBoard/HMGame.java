package GameBoard;

import Common.Game;
import UI.CommandType;
import UI.ConsoleColors;
import UI.UserInputs;

import static UI.GeneralPrints.printHorizontalLine;

public class HMGame extends Game<HMBoard> {
    private HMBoard board;
    private int dimension;

    public HMGame(int n){
        dimension = n;

    }
    public HMGame(){
        this(6);
    }

    @Override
    protected void initializeGame() {
        board = pickBoard(dimension);
    }

    @Override
    public void runGame() {
        initializeGame();
    }

    @Override
    public void printRules() {
        printHorizontalLine();
        System.out.println("These the rules.");
        printHorizontalLine();
        System.out.println();
    }

    private HMBoard pickBoard(int n){
        /*
        Allows the user to cycle through randomly generated boards before selecting one
         */
        HMBoard b;
        while(true){
            b = new HMBoard(n);
            b.displayBoard();
            ConsoleColors.printInColor(ConsoleColors.WHITE_BOLD, "Do you want to play on this map? (y,n)");
            String input = UserInputs.parseAndQuitIfAsked();

            if (UserInputs.isCommand(input, CommandType.YES)) break;

        }
        return b;
    }

}
