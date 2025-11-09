import Common.Game;
import GameBoard.HMBoard;
import GameBoard.HMGame;
import UI.CommandType;
import UI.ConsoleColors;
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
            System.out.println("Choose an Option:");
            System.out.println("[1] Play Heroes & Monsters");
            System.out.println("[2] Instructions");
            System.out.println("[3] Credits");
            System.out.print("Enter choice: ");
            String input = UserInputs.parseAndQuitIfAsked();

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice.\n");
                continue;
            }
            Game g = new HMGame();
            switch (choice) {
                case 1:
                    g.runGame();
                    break;
                case 2:
                    g.printRules();
                    break;
                case 3:
                    printCredits();
                    break;

                default:
                    System.out.println("Invalid choice.\n");
            }
            break;
        }
    }

    private static void printCredits(){
        ConsoleColors.printInColor(ConsoleColors.CYAN, "This was built by Owen Mariani in Novemeber of 2025. Check his GitHub @owenm-26");
    }


}
