package Common.Gameboard;
/*
FILE HEADER:
Abstracted logic to be shared by inherited game classes that have much overlap
 */

import Common.Figures.Hero.Hero;
import Common.Items.Inventory;
import Common.Items.Item;
import HeroesAndMonsters.GameBoard.HMGameState;
import Utility.UI.CommandType;
import Utility.UI.ConsoleColors;
import Utility.UI.UserInputs;

import java.util.List;
import java.util.Map;

import static Utility.UI.UserInputs.showMenuAndGetUserAnswer;


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
                throw new IllegalArgumentException("boardClass given does not have a constructor callable");
            }

            b.displayBoard();
            ConsoleColors.printInColor(ConsoleColors.WHITE_BOLD, "Do you want to play on this map? (y,n)");
            String input = UserInputs.parseAndQuitIfAsked();

            if (UserInputs.isCommand(input, CommandType.YES)) break;

        }
        return b;
    }

    protected static boolean selling(Hero h, Inventory marketInventory){
        Inventory i = h.getInventory();
        ConsoleColors.printInColor(ConsoleColors.BLUE_BOLD, "What type of item are you looking to sell?");
        List<? extends Item> subSection = i.selectInventorySubsection();
        if (subSection == null) return false;
        Map<String, ? extends Item> subSectionOptions = i.getSubInventoryOptions(subSection);
        String[] options = subSectionOptions.keySet().toArray(new String[0]);

        ConsoleColors.printInColor(ConsoleColors.BLUE_BOLD, "What are you trying to get rid of?");
        int chosenIndex = showMenuAndGetUserAnswer(options);

        if (chosenIndex < 0) return false;
        else{
            Item selectedItem = subSectionOptions.get(options[chosenIndex]);
            h.sellItem(selectedItem, marketInventory);
        }
        return true;
    }

    protected boolean buying(Hero h, Inventory marketInventory){
        ConsoleColors.printInColor(ConsoleColors.YELLOW, String.format("💰%s has %d gold left", h.getName(), h.getGold()));
        ConsoleColors.printInColor(ConsoleColors.BLUE_BOLD, "What type of item are you looking to buy?");
        List<? extends Item> subSection = marketInventory.selectInventorySubsection();
        if (subSection == null) return false;
        int heroLevel = h.getLevel();
        Map<String, ? extends Item> subSectionOptions = marketInventory.getSubInventoryOptions(subSection, heroLevel);
        String[] options = subSectionOptions.keySet().toArray(new String[0]);

        ConsoleColors.printInColor(ConsoleColors.BLUE_BOLD, "Pick what you like!");
        int chosenIndex = showMenuAndGetUserAnswer(options);

        if (chosenIndex < 0) return false;
        else{
            Item selectedItem = subSectionOptions.get(options[chosenIndex]);
            h.buyItem(selectedItem, marketInventory);
        }
        return true;
    }
}
