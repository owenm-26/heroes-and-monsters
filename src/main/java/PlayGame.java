import Common.Game;
import GameBoard.HMBoard;
import GameBoard.HMGame;
import UI.CommandType;
import UI.ConsoleColors;
import UI.GeneralPrints;
import UI.UserInputs;

import java.util.Scanner;

public class PlayGame {



    public static void main(String args[]){
        displayMenu();
    }

    private static void displayMenu(){
        System.out.println("\n--- Welcome to Heroes & Monsters ---");
        System.out.println("You can quit anytime by typing '" + CommandType.QUIT.getCode() + "'.\n");

        while (true) {
            String[] options = {"Play Heroes & Monsters", "Instructions", "Credits"};
            int choice = UserInputs.showMenuAndGetUserAnswer(options);

            Game g = new HMGame();
            boolean breakFlag = false;
            switch (choice) {
                case 0:
                    breakFlag=true;
                    g.runGame();
                    break;
                case 1:
                    g.printRules();
                    break;
                case 2 :
                    printCredits();
                    break;
                default:
                    System.out.println("Invalid choice.\n");
            }
            if (breakFlag) break;
        }
    }

    private static void printCredits(){
        ConsoleColors.printInColor(ConsoleColors.CYAN, "This was built by Owen Mariani in Novemeber of 2025. Check his GitHub @owenm-26");
        System.out.println();
    }


}
