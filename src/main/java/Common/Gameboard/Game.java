package Common.Gameboard;

import Utility.UI.CommandType;
import Utility.UI.ConsoleColors;
import Utility.UI.UserInputs;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

public abstract class Game<T extends Board> {
    protected T board;
    protected abstract void initializeGame();

    public abstract void runGame();

    public abstract void printRules();

    protected void endGame(){
        ConsoleColors.printInColor(ConsoleColors.BLACK_BACKGROUND, "☠️ Game Over. You lose.");
        System.exit(0);
    }

    public Board pickBoard(Class<? extends Board> boardClass, int n){
        /*
        Allows the user to cycle through randomly generated boards before selecting one
         */
        Board b;
        while(true){
            try{
                b = boardClass.getDeclaredConstructor().newInstance();
            }catch (Exception e){
                throw new ValueException("boardClass given does not have a constructor callable");
            }

            b.displayBoard();
            ConsoleColors.printInColor(ConsoleColors.WHITE_BOLD, "Do you want to play on this map? (y,n)");
            String input = UserInputs.parseAndQuitIfAsked();

            if (UserInputs.isCommand(input, CommandType.YES)) break;

        }
        return b;
    }
}
