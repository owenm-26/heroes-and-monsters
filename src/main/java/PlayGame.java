import Common.Gameboard.Game;
import HeroesAndMonsters.GameBoard.HMGame;
import LegendsOfValor.GameBoard.LVGame;
import Utility.UI.CommandType;
import Utility.UI.ConsoleColors;
import Utility.UI.UserInputs;

public class PlayGame {



    public static void main(String args[]){
        displayMenu();
    }

    private static void displayMenu(){
        System.out.println("\n--- Welcome ---");
        System.out.println("You can quit anytime by typing '" + CommandType.QUIT.getCode() + "'.\n");

        while (true) {
            String[] options = {"Play Heroes & Monsters", "Play Legends of Valor", "Heroes & Monsters Instructions", "Legends of Valor Instructions", "Credits"};
            int choice = UserInputs.showMenuAndGetUserAnswer(options);

            Game hm = new HMGame();
            Game lv = new LVGame();
            boolean breakFlag = false;
            switch (choice) {
                case 0:
                    breakFlag=true;
                    hm.runGame();
                    break;
                case 1:
                    breakFlag=true;
                    lv.runGame();
                    break;
                case 2:
                    hm.printRules();
                    break;
                case 3:
                    lv.printRules();
                    break;
                case 4 :
                    printCredits();
                    break;
                default:
                    System.out.println("Invalid choice.\n");
            }
            if (breakFlag) break;
        }
    }

    private static void printCredits(){
        ConsoleColors.printInColor(ConsoleColors.CYAN, "Heroes and Monsters was built by Owen Mariani in November of 2025. Check his GitHub @owenm-26\nHe built Legends of Valor with Jigar & Ali in December of 2025.");
        System.out.println();
    }


}
