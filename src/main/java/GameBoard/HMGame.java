package GameBoard;

import Common.Game;
import Figures.Hero.Hero;
import Figures.Party;
import UI.CommandType;
import UI.ConsoleColors;
import UI.UserInputs;

import java.util.*;

import static Figures.Hero.Hero.getAllHeroOptions;
import static UI.GeneralPrints.printHorizontalLine;
import static UI.UserInputs.*;

public class HMGame extends Game<HMBoard> {

    private final static int MAX_PARTY_SIZE=1;
    private HMBoard board;
    private int dimension;

    private HMGameState state;

    public HMGame(int n){
        dimension = n;
        state = HMGameState.EXPLORING;
    }
    public HMGame(){
        this(6);
    }

    @Override
    protected void initializeGame() {
        board = pickBoard(dimension);
        selectYourHeroes(MAX_PARTY_SIZE);
    }

    @Override
    public void runGame() {
        initializeGame();
        playGame();
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

    private Party<Hero> selectYourHeroes(int maxPartySize){
        /*
        Hero selection screen
         */
        Validators.Integers.validatePositiveIntegers(maxPartySize);
        List<Hero> options = getAllHeroOptions();
        Party<Hero> party = new Party<>(maxPartySize);

        while(options.size() > 0 && party.canAddAnotherMember()){
            ConsoleColors.printInColor(ConsoleColors.WHITE_BOLD, "Pick which hero(es) you want to add to your party!");
            Hero choice = getUserHeroChoice(options);
            party.addMember(choice);
            options.remove(choice);
        }
        return party;
    }

    private Hero getUserHeroChoice(List<Hero> optionsLeft){
        /*
        Randomly picks 3 heroes from optionsLeft and prints them along with the menu to select them or not
         */
        // Pick random choices to show user
        if (optionsLeft.size() < 1) throw new IllegalArgumentException("Hero optionsLeft List is empty");
        int number_of_choices = 3;
        String[] choices = new String[number_of_choices];
        Set<Integer> usedIndicies = new HashSet<>();
        int i =0;

        HashMap<Integer, Hero> backMap = new HashMap<>();
        while (i < number_of_choices){
            Random random = new Random();
            int randomIndex = random.nextInt(optionsLeft.size());
            if (usedIndicies.contains(randomIndex)) continue; //prevent printing the same character twice
            Hero h = optionsLeft.get(randomIndex);
            backMap.put(i, h);
            usedIndicies.add(randomIndex);
            choices[i] = h.getName();
            System.out.println(h);
            i++;
        }

        int heroChoiceIndex = showMenuAndGetUserAnswer(choices);
        return backMap.get(heroChoiceIndex);
    }

    private void playGame(){
        /*
        Handles all actions after picking map & characters through sub methods
         */

        while(true){
            switch (state){
                case EXPLORING:
                    exploring();
                    break;
                case MARKET:
                    market();
                    break;
                case BATTLING:
                    battling();
                    break;
                default:
                    throw new IllegalStateException("Impossible Game State. Something went wrong in playGame()");
            }
        }
    }

    private void exploring(){
        while (state == HMGameState.EXPLORING){
            board.displayBoard();
            ConsoleColors.printInColor(ConsoleColors.GREEN_BOLD, "Press WASD to move or check inventory with i");
            String input = UserInputs.parseAndQuitIfAsked();

            if(!isAnyCommand(input)) {
                System.out.println("Invalid Command.");
                continue;
            }
            if(isMovement(input)){
                board.handleMovement(input);
            }

        }
    }

    private void market(){
        while (state == HMGameState.MARKET){

        }
    }

    private void battling(){
        while (state == HMGameState.BATTLING){

        }
    }

}
