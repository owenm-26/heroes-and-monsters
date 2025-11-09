import GameBoard.Board;
import GameBoard.Game;
import UI.CommandType;
import UI.ConsoleColors;
import UI.UserInputs;

import java.util.Scanner;

public class PlayGame {

    private static final Scanner scanner = new Scanner(System.in);


    public static void main(String args[]){
        Board b = pickBoard();
        Game g = new Game(b);
    }

    private static Board pickBoard(){

        Board b;
        while(true){
            b = new Board();
            b.displayBoard();
            ConsoleColors.printInColor(ConsoleColors.WHITE_BOLD, "Do you want to play on this map? (y,n)");
            String input = scanner.next();



            if (UserInputs.isCommand(input, CommandType.YES)) break;

        }

        return b;

    }
}
