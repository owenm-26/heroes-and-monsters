package LegendsOfValor.GameBoard;

import Common.Figures.Figure;
import Common.Figures.Hero.Hero;
import Common.Figures.Monster.Monster;
import Common.Figures.Party;
import Common.Gameboard.Game;
import Common.Items.Inventory;
import Common.Items.ItemType;
import Common.MarketActions;
import HeroesAndMonsters.Battle.BattleCommand;
import HeroesAndMonsters.GameBoard.HMGameState;
import LegendsOfValor.GameBoard.LVSquare.LVSquare;
import LegendsOfValor.GameBoard.Menus.LVMovementMenuOptions;
import Utility.UI.CommandType;
import Utility.UI.ConsoleColors;
import LegendsOfValor.GameBoard.Menus.LVMainMenuOptions;
import Utility.UI.UserInputs;

import java.util.*;

import static Common.Figures.Party.pickTeamAndDisplayStatistics;
import static Utility.UI.UserInputs.showMenuAndGetUserAnswer;

public class LVGame extends Game<LVBoard> {

    private Party<Hero> heroes;

    private Map<Figure, Integer> figureOriginalLanes;
    private Party<Monster> monsters;

    @Override
    protected void initializeGame() {
        ConsoleColors.printInColor(ConsoleColors.CYAN_BOLD,
                "\n--- Initializing Legends of Valor ---\n");

        // 1. Let user pick a map
        this.board = (LVBoard) pickBoard(LVBoard.class, LVBoard.SIZE);

        // 2. Hero selection
        ConsoleColors.printInColor(ConsoleColors.GREEN_BOLD, "Select your 3 heroes:");
        heroes = Hero.selectYourHeroes(3);

        // 3. Place heroes (H1/H2/H3) at bottom Nexus
        placeHeroes();

        // 4. Spawn monsters at top Nexus (one per lane)
        spawnMonsters();
    }

    private void placeHeroes() {
        figureOriginalLanes = new HashMap<>();
        int row = LVBoard.SIZE - 1;      // bottom Nexus row = 7
        int[] cols = {0, 3, 6};          // lane 0,1,2

        int i = 0;
        for (Hero h : heroes.getMembers()) {
            String label = "H" + (i + 1);    // H1, H2, H3
            figureOriginalLanes.put(h, (i+1));
            board.placeHero(h, row, cols[i], label);
            i++;
        }
    }

    private void spawnMonsters() {
        monsters = new Party<>(100);
        List<Monster> pool = Monster.getAllMonsterOptions();

        // Pick 3 random monsters
        for (int i = 0; i < 3; i++) {
            Monster m = pool.get((int) (Math.random() * pool.size()));
            monsters.addMember(m);
        }

        int row = 0;                // top Nexus
        int[] cols = {1, 4, 7};     // columns for lane 0,1,2 monsters

        List<Monster> monsterList = monsters.getMembers();
        for (int i = 0; i < 3; i++) {
            board.placeMonster(monsterList.get(i), row, cols[i]);
            figureOriginalLanes.put(monsterList.get(i), (i+1));
        }
    }

    @Override
    public void runGame() {
        if (this.board == null) {
            initializeGame();
        }

        ConsoleColors.printInColor(ConsoleColors.YELLOW_BOLD,
                "\nLegends of Valor setup complete! Movement enabled.\n");

        boolean gameRunning = true;
        board.displayBoard();
        while (gameRunning) {
            heroesTeamTurn();
            monstersTeamTurn();
        }
    }

    @Override
    public void printRules() {
        ConsoleColors.printInColor(ConsoleColors.WHITE_BOLD,
                "Legends of Valor Rules (see assignment PDF).");
    }

    // ============================================================
    // ======================= HERO TURN ==========================
    // ============================================================

    private void heroesTeamTurn(){
        for (Hero h: heroes.getMembers()) {
            heroTurn(h);
            board.displayBoard();
            //TODO: check win
        }
    }

    private void monstersTeamTurn(){
        for (Monster m: monsters.getMembers()){
            monsterTurn(m);
        }
        board.displayBoard();
        //TODO: check lose
    }

    private void heroTurn(Hero h) {
        int laneIndex = figureOriginalLanes.get(h);
        ConsoleColors.printInColor(ConsoleColors.BLUE_BOLD,
                "\n---- Lane " + (laneIndex) + " Hero Turn ----");

        int[] pos = board.getHeroPosition(h);
        if (pos != null) {
            ConsoleColors.printInColor(ConsoleColors.CYAN,
                    "Hero: " + h.getName() +
                            " (H" + (laneIndex) + ")" +
                            " at (" + pos[0] + ", " + pos[1] + ")");
        }

        boolean actionTaken = false;

        while (!actionTaken) {
            actionTaken = presentOptionsAndUndertakeUserChoice(h, pos);
        }
    }

    private boolean presentOptionsAndUndertakeUserChoice(Hero h, int[] position) {

        ArrayList<String> options = new ArrayList<>(Arrays.asList(LVMainMenuOptions.MOVE.getCode(), LVMainMenuOptions.BACKPACK.getCode(), LVMainMenuOptions.STATS.getCode()));

        // check if on Nexus
        if (board.getSquareInventory(position[0], position[1]) != null){
            options.add(LVMainMenuOptions.MARKET.getCode());
        }
        // gather all monsters around and all obstacles around
        ArrayList<Monster> monstersInRange = board.getAllMonstersInAttackRange(position[0], position[1]);
        ArrayList<LVSquare> obstaclesInRange = board.getAllObstaclesInRange(position[0], position[1]);

        if (monstersInRange.size() > 0) options.add(LVMainMenuOptions.ATTACK.getCode());
        if (obstaclesInRange.size() > 0) options.add(LVMainMenuOptions.REMOVE_OBSTACLE.getCode());
        options.add(LVMainMenuOptions.PASS.getCode());

        boolean turnUsed = false;

        while (!turnUsed){
            int index = -1;
            while(index < 0){
                index = UserInputs.showMenuAndGetUserAnswer(options.toArray(new String[0]));
                if (index < 0) ConsoleColors.printInColor(ConsoleColors.RED ,"Cannot go back in this menu");
            }

            //Handle Choice
            String choice = options.get(index);
            switch (LVMainMenuOptions.fromName((choice))){
                case MOVE:
                    turnUsed = move(h);
                    break;
                case BACKPACK:
                    turnUsed = backpack(h);
                    break;
                case STATS:
                    Map<String, Party<? extends Figure>> teams = new HashMap<>();
                    teams.put("Heroes", heroes);
                    teams.put("Monsters", monsters);
                    pickTeamAndDisplayStatistics(teams);
                    break;
                case MARKET:
                    market(h, board.getSquareInventory(position[0], position[1]));
                    break;
                case REMOVE_OBSTACLE:
                    //TODO: Implement Remove Obstacle helper
                    turnUsed = true;
                    break;
                case ATTACK:
                    //TODO: Implement Attack helper
                    turnUsed = true;
                    break;
                case PASS:
                    ConsoleColors.printInColor(ConsoleColors.YELLOW,
                            "Hero passes the turn.");
                    turnUsed = true;
                    break;
            }
        }

        return turnUsed;
    }

    private boolean move(Hero h){
        ArrayList<String> options = new ArrayList<>();

        for(LVMovementMenuOptions o: LVMovementMenuOptions.values()){
            options.add(o.getCode());
        }

        int index = UserInputs.showMenuAndGetUserAnswer(options.toArray(new String[0]));
        if (index < 0) return false;
        boolean actionTaken = false;

        switch (LVMovementMenuOptions.fromName(options.get(index))){
            case MOVE:
                String[] movements = UserInputs.MOVEMENT_COMMANDS.toArray(new String[0]);
                String useMovementInput = "";
                while(true){
                    ConsoleColors.printInColor(ConsoleColors.WHITE_BOLD, String.format("Which direction would you like to move? (%s)", Arrays.toString(movements)));
                    useMovementInput = UserInputs.parseAndQuitIfAsked();
                    if(!UserInputs.isMovement(useMovementInput)){
                        ConsoleColors.printInColor(ConsoleColors.RED, "Invalid command entered.");
                        continue;
                    }
                    break;
                }
                actionTaken = attemptMove(h, useMovementInput);
                break;
            case TELEPORT:
                actionTaken = attemptTeleport(h);
                break;
            case RECALL:
                actionTaken = attemptRecall(h);
                break;
        }

        return actionTaken;

    }

    private boolean backpack(Hero h){
        /*
        Only ever takes a turn if the user uses a potion
         */

        //Display current Equipped and stats
        h.displayFigureStatistics(HMGameState.BATTLING);
        //Ask them what they want to do (equip, unequip, take potion)
        String[] options = BattleCommand.getBackpackActions();
        int indexChosen = showMenuAndGetUserAnswer(options);
        if (indexChosen < 0) return false;

        boolean turnTaken = false;

        switch (BattleCommand.fromName(options[indexChosen])){
            case CHANGE_WEAPON:
                h.equipItem(ItemType.WEAPON);
                break;
            case CHANGE_ARMOR:
                h.equipItem(ItemType.ARMOR);
                break;
            case TAKE_POTION:
                turnTaken = h.equipItem(ItemType.POTION);
                break;
        }

        return turnTaken;
    }

    private boolean market(Hero h, Inventory marketInventory){
        ConsoleColors.printInColor(ConsoleColors.BLUE_BOLD, "What do you want to do in market?");
        String[] options = MarketActions.marketActionsToList();
        int indexChosen = showMenuAndGetUserAnswer(options);

        if (indexChosen < 0) return false;
        MarketActions chosenAction = MarketActions.fromName(options[indexChosen]);

        switch (chosenAction) {

            case BUY:
                buying(h, marketInventory);
                break;

            case SELL:
                selling(h, marketInventory);
                break;

            default:
                System.out.println("Unknown action chosen.");
        }
        return false;
    }

    private boolean attemptMove(Hero h, String input) {
        CommandType direction = CommandType.fromName(input);
        boolean success = board.moveHero(h, input);
        if (!success) {
            ConsoleColors.printInColor(ConsoleColors.RED,
                    "Illegal move. You cannot move there.");
        } else {
            ConsoleColors.printInColor(ConsoleColors.GREEN,
                    "Hero moved " + direction.name().toLowerCase() + ".");
        }
        return success;
    }

    private boolean attemptTeleport(Hero h) {
        // Build list of other heroes as teleport targets
        List<Hero> options = new ArrayList<>();
        List<String> names = new ArrayList<>();

        for (Hero other : heroes.getMembers()) {
            if (other != h) {
                options.add(other);
                String name = String.format("%s [H%s]", other.getName(), figureOriginalLanes.get(other));
                names.add(name);
            }
        }

        if (options.isEmpty()) {
            ConsoleColors.printInColor(ConsoleColors.RED,
                    "No other heroes available to teleport to.");
            return false;
        }

        ConsoleColors.printInColor(ConsoleColors.PURPLE_BOLD,
                "Choose a hero to teleport next to:");

        String[] choices = names.toArray(new String[0]);
        int idx = UserInputs.showMenuAndGetUserAnswer(choices);
        if (idx < 0) {
            return false;
        }

        Hero target = options.get(idx);
        boolean success = board.teleportHero(h, target);

        if (!success) {
            ConsoleColors.printInColor(ConsoleColors.RED,
                    "Teleport failed. No legal adjacent space. Cannot teleport to Hero in same lane.");
        } else {
            ConsoleColors.printInColor(ConsoleColors.GREEN,
                    "Teleported next to " + target.getName() + ".");
        }
        return success;
    }

    private boolean attemptRecall(Hero h) {
        boolean success = board.recallHero(h);
        if (!success) {
            ConsoleColors.printInColor(ConsoleColors.RED,
                    "Recall failed. Nexus is blocked or invalid.");
        } else {
            ConsoleColors.printInColor(ConsoleColors.GREEN,
                    "Hero recalled to their Nexus.");
        }
        return success;
    }

    // ============================================================
    // ====================== MONSTER TURN ========================
    // ============================================================

    private void monsterTurn(Monster m) {
        int laneIndex = figureOriginalLanes.get(m);
        ConsoleColors.printInColor(ConsoleColors.BLUE,
                "\n---- Lane " + (laneIndex) + " Monster Turn ----");

        int[] pos = board.getMonsterPosition(m);
        if (pos != null) {
            ConsoleColors.printInColor(ConsoleColors.CYAN,
                    "Monster: " + m.getName() +
                            " at (" + pos[0] + ", " + pos[1] + ")");
        }

        boolean moved = board.moveMonsterForward(m);

        //TODO: if they can't move they should try to attack
        if (!moved) {
            ConsoleColors.printInColor(ConsoleColors.YELLOW,
                    "Monster stays in place (blocked or at edge).");
        } else {
            ConsoleColors.printInColor(ConsoleColors.GREEN,
                    "Monster advances toward heroes' Nexus.");
        }
    }
}
