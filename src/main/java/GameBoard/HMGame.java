package GameBoard;

import Battle.Battle;
import Battle.BattleCommand;
import Common.Game;
import Figures.Figure;
import Figures.Hero.Hero;
import Figures.Monster.Monster;
import Figures.Party;
import GameBoard.HMSquare.MarketActions;
import Items.Inventory;
import Items.Item;
import Items.ItemType;
import Items.Weapon;
import UI.CommandType;
import UI.ConsoleColors;
import UI.UserInputs;

import java.util.*;

import static Figures.Hero.Hero.getAllHeroOptions;
import static Figures.Party.pickTeamAndDisplayStatistics;
import static GameBoard.HMSquare.MarketActions.marketActionsToList;
import static UI.GeneralPrints.printHorizontalLine;
import static UI.UserInputs.*;

public class HMGame extends Game<HMBoard> {

    private final static int MAX_PARTY_SIZE=3;
    private HMBoard board;
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
        board = pickBoard(dimension);
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
        printHorizontalLine();
        System.out.println("MONSTERS AND HEROES - GAME RULES\n" +
                "\n" +
                "Goal of the Game\n" +
                "Explore the world, fight monsters, earn gold, gain experience, and level up your heroes. If all heroes faint, the game ends.\n" +
                "\n" +
                "World Layout\n" +
                "The world is a grid made of three types of tiles:\n" +
                "Common tiles: normal areas where battles may occur.\n" +
                "Market tiles: shops where heroes can buy and sell items.\n" +
                "Inaccessible tiles: cannot be entered.\n" +
                "You can move the party up, down, left, or right.\n" +
                "\n" +
                "Heroes\n" +
                "You start by choosing 1 to 3 heroes.\n" +
                "Each hero has: name, level, experience, HP (health), MP (mana), strength, dexterity, agility, gold, and an inventory.\n" +
                "Hero classes:\n" +
                "Warriors focus on strength and agility.\n" +
                "Sorcerers focus on dexterity and agility.\n" +
                "Paladins focus on strength and dexterity.\n" +
                "\n" +
                "Monsters\n" +
                "Monsters appear during battles.\n" +
                "They match the level of your strongest hero.\n" +
                "Each has: name, level, HP, damage, defense, and dodge chance.\n" +
                "Monster types:\n" +
                "Dragons have higher damage.\n" +
                "Exoskeletons have higher defense.\n" +
                "Spirits have higher dodge.\n" +
                "\n" +
                "Markets\n" +
                "Heroes can enter a market when standing on a market tile.\n" +
                "Heroes shop individually; gold is not shared.\n" +
                "Items include weapons, armor, potions, and spells.\n" +
                "Heroes may only buy items they can afford and that are not above their level.\n" +
                "Items sell for half their original price.\n" +
                "\n" +
                "Battles\n" +
                "Battles occur randomly on common tiles.\n" +
                "Each battle has the same number of monsters as heroes.\n" +
                "Heroes always act first. Each hero can choose one action:\n" +
                "Attack with a weapon\n" +
                "Cast a spell\n" +
                "Use a potion\n" +
                "Equip weapon or armor\n" +
                "View stats (does not use a turn)\n" +
                "Monsters then attack the heroes.\n" +
                "Heroes or monsters may dodge attacks.\n" +
                "If a hero reaches 0 HP, they faint and cannot act until the battle ends.\n" +
                "If all heroes faint, the game is over.\n" +
                "If all monsters are defeated, heroes win.\n" +
                "\n" +
                "After Each Round\n" +
                "Heroes who are still standing regain 10 percent HP and 10 percent MP.\n" +
                "\n" +
                "Rewards\n" +
                "If heroes win the battle:\n" +
                "Heroes who did not faint gain gold and experience.\n" +
                "Fainted heroes revive after the battle with half HP and half MP, but they do not gain rewards.\n" +
                "\n" +
                "Leveling Up\n" +
                "When a hero gains enough experience, they level up.\n" +
                "All stats increase by 5 percent and their favored stats increase by an additional 5 percent.\n" +
                "HP becomes: level times 100.\n" +
                "MP increases by 10 percent.\n" +
                "\n" +
                "Controls\n" +
                "W: move up\n" +
                "A: move left\n" +
                "S: move down\n" +
                "D: move right\n" +
                "M: enter market (only on market tile)\n" +
                "I: show hero and monster information\n" +
                "Q: quit the game\n" +
                "\n" +
                "Winning and Losing\n" +
                "You win battles by defeating all monsters.\n" +
                "You lose the game if all heroes faint.");

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
        Set<Integer> usedIndices = new HashSet<>();
        int i =0;

        HashMap<Integer, Hero> backMap = new HashMap<>();
        while (i < number_of_choices){
            Random random = new Random();
            int randomIndex = random.nextInt(optionsLeft.size());
            if (usedIndices.contains(randomIndex)) continue; //prevent printing the same character twice
            Hero h = optionsLeft.get(randomIndex);
            backMap.put(i, h);
            usedIndices.add(randomIndex);
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

    private void endGame(){
        ConsoleColors.printInColor(ConsoleColors.BLACK_BACKGROUND, "☠️ Game Over. You lose.");
        System.exit(0);
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
            String[] options = marketActionsToList();
            int indexChosen = showMenuAndGetUserAnswer(options);

            if (indexChosen < 0) {
                state = HMGameState.EXPLORING;
                break;
            }
            MarketActions chosenAction = MarketActions.fromName(options[indexChosen]);

            switch (chosenAction) {
                case EXIT:
                    state = HMGameState.EXPLORING;
                    break;

                case BUY:
                    buying(h, marketInventory);
                    break;

                case SELL:
                    selling(h, marketInventory);
                    break;

                default:
                    System.out.println("Unknown action chosen.");
            }


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

    private void buying(Hero h, Inventory marketInventory){
        ConsoleColors.printInColor(ConsoleColors.YELLOW, String.format("💰%s has %d gold left", h.getName(), h.getGold()));
        ConsoleColors.printInColor(ConsoleColors.BLUE_BOLD, "What type of item are you looking to buy?");
        List<? extends Item> subSection = marketInventory.selectInventorySubsection();
        if (subSection == null) return;
        int heroLevel = h.getLevel();
        Map<String, ? extends Item> subSectionOptions = marketInventory.getSubInventoryOptions(subSection, heroLevel);
        String[] options = subSectionOptions.keySet().toArray(new String[0]);

        ConsoleColors.printInColor(ConsoleColors.BLUE_BOLD, "Pick what you like!");
        int chosenIndex = showMenuAndGetUserAnswer(options);

        if (chosenIndex < 0)state = HMGameState.EXPLORING;
        else{
            Item selectedItem = subSectionOptions.get(options[chosenIndex]);
            h.buyItem(selectedItem, marketInventory);
        }
    }

    private void selling(Hero h, Inventory marketInventory){
        Inventory i = h.getInventory();
        ConsoleColors.printInColor(ConsoleColors.BLUE_BOLD, "What type of item are you looking to sell?");
        List<? extends Item> subSection = i.selectInventorySubsection();
        if (subSection == null) return;
        Map<String, ? extends Item> subSectionOptions = i.getSubInventoryOptions(subSection);
        String[] options = subSectionOptions.keySet().toArray(new String[0]);

        ConsoleColors.printInColor(ConsoleColors.BLUE_BOLD, "What are you trying to get rid of?");
        int chosenIndex = showMenuAndGetUserAnswer(options);

        if (chosenIndex < 0)state = HMGameState.EXPLORING;
        else{
            Item selectedItem = subSectionOptions.get(options[chosenIndex]);
            h.sellItem(selectedItem, marketInventory);
        }
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
