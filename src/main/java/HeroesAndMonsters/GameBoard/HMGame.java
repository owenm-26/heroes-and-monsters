package HeroesAndMonsters.GameBoard;

import Common.Figures.Figure;
import Common.Figures.Party;
import Common.Figures.Hero.Hero;
import Common.Figures.Monster.Monster;
import Common.Gameboard.Board;
import Common.Gameboard.Game;
import Common.Gameboard.Square;
import Common.Items.Inventory;
import Common.Items.Item;
import Common.Items.ItemType;
import HeroesAndMonsters.Battle.Battle;
import HeroesAndMonsters.Battle.BattleCommand;
import Common.MarketActions;
import Utility.UI.CommandType;
import Utility.UI.ConsoleColors;
import Utility.UI.GeneralPrints;
import Utility.UI.UserInputs;
import Utility.Validators.Integers;


import java.util.*;

import static Common.Figures.Hero.Hero.getAllHeroOptions;
import static Common.Figures.Hero.Hero.selectYourHeroes;
import static Common.Figures.Party.pickTeamAndDisplayStatistics;
import static Utility.UI.GeneralPrints.printHorizontalLine;
import static Utility.UI.UserInputs.*;

public class HMGame extends Game<HMBoard> {

    private final static int MAX_PARTY_SIZE=3;
    private final int dimension;

    private HMGameState state;
    private boolean viewingStatistics;

    private Map<String, Party<? extends Figure>> parties;

    public HMGame(int n){
        dimension = n;
        state = HMGameState.EXPLORING;
        viewingStatistics = false;
    }
    public HMGame(){
        this(6);
    }

    @Override
    protected void initializeGame() {
        board = (HMBoard)pickBoard(HMBoard.class, dimension);
        parties = new LinkedHashMap<>();
        parties.put("Heroes", selectYourHeroes(MAX_PARTY_SIZE));
    }

    @Override
    public void runGame() {
        initializeGame();
        playGame();
    }

    @Override
    public void printRules() {
        GeneralPrints.printRules("HMrules.txt");
    }

    private void playGame(){
        /*
        Handles all actions after picking map & characters through sub methods
         */

        while(true){

            if(viewingStatistics) {
                viewStats();
                continue;
            }

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
            if(viewingStatistics){
                viewStats();
                continue;
            }
            board.displayBoard();
            ConsoleColors.printInColor(ConsoleColors.GREEN_BOLD, "Press WASD to move or check inventory with i");
            String input = UserInputs.toggleInventoryParseAndQuitIfAsked(this);

            if(isCommand(input, CommandType.MARKET) && board.partyIsInMarket()) {
                state = HMGameState.MARKET;
                continue;
            }
            if (isCommand(input, CommandType.BACKPACK)){
                backpack();
            }
            if(!isExploringCommand(input)) {
                System.out.println("Invalid Command.");
                continue;
            }
            if(isMovement(input)){
                board.handleMovement(input, this);
            }

        }
    }

    private void backpack(){
        /*
        Only accessible when exploring. Allows user to equip / unequip and use potions.
         */
        //Pick Hero to view
        Hero h = (Hero) parties.get("Heroes").pickTeamMember(this, "Which team member's backpack?");
        //Display current Equipped and stats
        h.displayFigureStatistics(HMGameState.BATTLING);
        //Ask them what they want to do (equip, unequip, take potion)
        String[] options = BattleCommand.getBackpackActions();
        int indexChosen = showMenuAndGetUserAnswer(options);
        if (indexChosen < 0) return;

        switch (BattleCommand.fromName(options[indexChosen])){
            case CHANGE_WEAPON:
                h.equipItem(ItemType.WEAPON);
                break;
            case CHANGE_ARMOR:
                h.equipItem(ItemType.ARMOR);
                break;
            case TAKE_POTION:
                h.equipItem(ItemType.POTION);
        }
    }

    public void viewStats(){
        /*
        Shows two different views of the Stats:
        - exploring
        - battling
         */
        if (parties.size() < 1 ) throw new IllegalStateException("Parties state is empty. This is impossible. Should always exist heroes.");
        pickTeamAndDisplayStatistics(parties, this);
        toggleStatistics();
    }


    private void market(){
        Inventory marketInventory = board.getMarketInventory();

        while (state == HMGameState.MARKET){
            Hero h = (Hero) parties.get("Heroes").pickTeamMember(this, "Which team member is going to enter the market?");

            if (h == null) {
                state = HMGameState.EXPLORING;
                break;
            }
            ConsoleColors.printInColor(ConsoleColors.BLUE_BOLD, "What do you want to do in market?");
            String[] options = MarketActions.marketActionsToList();
            int indexChosen = showMenuAndGetUserAnswer(options);

            if (indexChosen < 0) {
                state = HMGameState.EXPLORING;
                break;
            }
            MarketActions chosenAction = MarketActions.fromName(options[indexChosen]);
            boolean exiting = false;
            switch (chosenAction) {

                case BUY:
                    exiting = buying(h, marketInventory);
                    break;

                case SELL:
                    exiting = Game.selling(h, marketInventory);
                    break;

                default:
                    System.out.println("Unknown action chosen.");
            }

            if (exiting) state = HMGameState.EXPLORING;


        }
    }

    private void battling(){
        Battle b = new Battle((Party<Hero>) parties.get("Heroes"), this);
        Party<Monster> monsters = b.getMonsters();
        parties.put("Monsters", monsters);
        boolean won = b.executeBattle();
        if (!won) endGame();
        b.handleHeroWinAndRewards();
        parties.remove("Monsters");
        state = HMGameState.EXPLORING;
    }

    public void toggleStatistics() {
        this.viewingStatistics = !viewingStatistics;
    }

    public boolean isViewingStatistics() {
        return viewingStatistics;
    }

    public HMGameState getState() {
        return state;
    }

    public void setState(HMGameState state) {
        this.state = state;
    }
}
