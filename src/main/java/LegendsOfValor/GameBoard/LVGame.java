package LegendsOfValor.GameBoard;
/*
FILE HEADER:
All logic for playing LegendsOfValor, with its own LVBoard and Hero Parties and Monster Parties
 */

import Common.Figures.Figure;
import Common.Figures.Hero.Hero;
import Common.Figures.Monster.Monster;
import Common.Figures.Party;
import Common.Gameboard.Game;
import Common.Items.DamageDealing;
import Common.Items.Inventory;
import Common.Items.ItemType;
import Common.MarketActions;
import HeroesAndMonsters.Battle.Battle;
import HeroesAndMonsters.Battle.BattleCommand;
import HeroesAndMonsters.GameBoard.HMGameState;
import LegendsOfValor.GameBoard.LVSquare.LVSquare;
import LegendsOfValor.GameBoard.Menus.LVMovementMenuOptions;
import Utility.UI.CommandType;
import Utility.UI.ConsoleColors;
import LegendsOfValor.GameBoard.Menus.LVMainMenuOptions;
import Utility.UI.GeneralPrints;
import Utility.UI.UserInputs;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static Common.Figures.Monster.Monster.assembleMonsterBattleParty;
import static Common.Figures.Party.pickTeamAndDisplayStatistics;
import static Utility.UI.UserInputs.showMenuAndGetUserAnswer;

public class LVGame extends Game<LVBoard> {

    private Party<Hero> heroes;

    private Map<Figure, Integer> figureOriginalLanes;
    private Party<Monster> monsters;

    private Set<Monster> monstersSeen;
    private Party<Hero> toRespawn;

    private int round;

    @Override
    protected void initializeGame() {
        ConsoleColors.printInColor(ConsoleColors.CYAN_BOLD,
                "\n--- Initializing Legends of Valor ---\n");

        // 1. Let user pick a map
        this.board = (LVBoard) pickBoard(LVBoard.class, LVBoard.SIZE);

        // 2. Hero selection
        ConsoleColors.printInColor(ConsoleColors.GREEN_BOLD, "Select your 3 heroes:");
        heroes = Hero.selectYourHeroes(3);
        toRespawn = new Party<>(heroes.size());
        monstersSeen = new HashSet<>();
        round = 1;

        // 3. Place heroes (H1/H2/H3) at bottom Nexus
        GeneralPrints.printHorizontalLine();
        placeHeroes();

        // 4. Spawn monsters at top Nexus (one per lane)
        spawnOriginalMonsters();
    }

    @Override
    protected void endGame() {
        System.exit(0);
    }

    private void placeHeroes() {
        figureOriginalLanes = new HashMap<>();
        int row = LVBoard.SIZE - 1;      // bottom Nexus row = 7

        LinkedHashMap<Integer, Integer> laneOptions = new LinkedHashMap<>();
        laneOptions.put(1,0); laneOptions.put(2,3); laneOptions.put(3,6);
        Integer[] optionIndexes;
        String[] options;

        int i = 0;
        while (i < heroes.size() && !laneOptions.isEmpty()) {
            optionIndexes = laneOptions.keySet().toArray(new Integer[0]);
            options = laneOptions.keySet().stream().map(e -> String.format("Lane %d", e)).collect(Collectors.toList()).toArray(new String[0]);
            Hero h = heroes.getMembers().get(i);
            ConsoleColors.printInColor(ConsoleColors.WHITE, String.format("Which lane would you like to put %s in?", h.getName()));
            int laneChoice = UserInputs.showMenuAndGetUserAnswer(options);
            if (laneChoice < 0) {
                ConsoleColors.printInColor(ConsoleColors.RED, "Invalid Choice. Please try again");
                continue;
            }
            int actualChoice  = optionIndexes[laneChoice];

            // Give them the lane #
            String label = "H" + actualChoice;    // H1, H2, H3
            figureOriginalLanes.put(h, actualChoice);

            // Place the hero in the correct col
            board.placeHero(h, row, laneOptions.get(actualChoice), label);

            // remove from options
            laneOptions.remove(actualChoice);
            i++;
        }

        if(i < heroes.size()){
            throw new IllegalArgumentException("Hero Party Size is too large to fit on the board such that only one hero is in each lane");
        }
    }

    private void spawnOriginalMonsters() {
        monsters = assembleMonsterBattleParty(heroes);
        monstersSeen.addAll(monsters.getMembers());

        int row = 0;                // top Nexus
        int[] cols = {1, 4, 7};     // columns for lane 0,1,2 monsters

        List<Monster> monsterList = monsters.getMembers();
        for (int i = 0; i < 3; i++) {
            board.placeMonster(monsterList.get(i), row, cols[i]);
            figureOriginalLanes.put(monsterList.get(i), (i+1));
        }
    }

    private void spawnNewMonsterWave(){
        Party<Monster> newWave = assembleMonsterBattleParty(heroes, monstersSeen);
        int[] cols = {1, 4, 7};     // columns for lane 0,1,2 monsters

        boolean printedAlready = false; boolean success;
        for(int i=0; i< newWave.size(); i++){
            Monster m =newWave.getMembers().get(i);
            success = board.placeMonster(m, 0, cols[i]);
            if (!printedAlready){
                ConsoleColors.printInColor(ConsoleColors.RED_BOLD, "New monsters have spawned in their Nexus!");
                printedAlready = true;
            }
            if (success){
                figureOriginalLanes.put(m, (i+1));
                ConsoleColors.printInColor(ConsoleColors.RED, String.format("%s has spawned", m.getName()));
            }
        }

        Party.combineTwoParties(monsters, newWave);



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
            ConsoleColors.printInColor(ConsoleColors.BLACK_BACKGROUND, String.format("--- Round %d ---", round));
            if(round % 10 == 0) spawnNewMonsterWave();
            heroesTeamTurn();
            monstersTeamTurn();
            round++;
        }
    }

    @Override
    public void printRules() {
        GeneralPrints.printRules(
                "LVrules.txt"
        );
    }

    // ============================================================
    // ======================= HERO TURN ==========================
    // ============================================================

    private void heroesTeamTurn(){
        boolean won = false;

        // respawn all dead heroes
        Iterator<Hero> it = toRespawn.getMembers().iterator();
        boolean first = true;
        while (it.hasNext()) {
            if (first) {
                ConsoleColors.printInColor(ConsoleColors.PURPLE_BACKGROUND, "\nReviving Heroes:");
                first = false;
            }
            Hero h = it.next();
            boolean success = board.respawnHero(h);
            if (success) {

                heroes.addMember(h, false);
                it.remove();
            }
        }

        // give all heroes back 10% health and mana
        for (Hero h: heroes.getMembers()){
            h.gainMp((int) (h.getMpMax()*0.1));
            h.gainHp((int) (h.getHpMax()*0.1));
        }

        board.displayBoard();

        //go through turns in lane order
        List<Hero> heroesInLaneOrder = figureOriginalLanes.entrySet().stream()
                .filter(e -> e.getKey() instanceof Hero)
                .sorted(Map.Entry.comparingByValue()) // sort by lane
                .map(e -> (Hero) e.getKey())
                .collect(Collectors.toList());

        for (Hero h: heroesInLaneOrder) {
            heroTurn(h);
            board.displayBoard();
            if (board.figureIsInGoalSquare(h)) {
                won = true;
                break;
            }
        }
        if (won){
            ConsoleColors.printInColor(ConsoleColors.YELLOW_BACKGROUND, "🏆 You reached the monsters' Nexus, you win!");
            endGame();
        }

    }

    private void monstersTeamTurn(){
        boolean won = false;

        //go through turns in lane order
        List<Monster> monstersInLaneOrder = figureOriginalLanes.entrySet().stream()
                .filter(e -> e.getKey() instanceof Monster)
                .sorted(Map.Entry.comparingByValue()) // sort by lane
                .map(e -> (Monster) e.getKey())
                .collect(Collectors.toList());
        for (Monster m: monstersInLaneOrder){
            int laneIndex = figureOriginalLanes.get(m);
            ConsoleColors.printInColor(ConsoleColors.BLUE,
                    "\n---- Lane " + (laneIndex) + " Monster(s) Turn ----");
            monsterTurn(m);
            if (board.figureIsInGoalSquare(m)) {
                won = true;
                break;
            }
        }

        if (won){
            board.displayBoard();
            ConsoleColors.printInColor(ConsoleColors.RED_BACKGROUND, "☠️A monster reached your Nexus. You Lose");
            endGame();
        }

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
        h.decrementTimeOnAllEffects();
    }

    private boolean presentOptionsAndUndertakeUserChoice(Hero h, int[] position) {

        ArrayList<String> options = new ArrayList<>(Arrays.asList(LVMainMenuOptions.MOVE.getCode(), LVMainMenuOptions.BACKPACK.getCode(), LVMainMenuOptions.STATS.getCode()));

        // check if on Nexus
        if (board.getSquareInventory(position[0], position[1]) != null){
            options.add(LVMainMenuOptions.MARKET.getCode());
        }
        // gather all monsters around and all obstacles around
        ArrayList<Monster> monstersInRange = board.getAllFiguresInAttackRange(position[0], position[1], Monster.class);
        LinkedHashMap<LVSquare, Integer[]> obstaclesInRange = board.getAllObstaclesInRange(position[0], position[1]);

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
                    turnUsed = removeObstacle(obstaclesInRange);
                    break;
                case ATTACK:
                    turnUsed = attack(h, monstersInRange);
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

    private boolean attack(Figure f, ArrayList<? extends Figure> victims){
        /*
        Single method to handle attacks of Heroes and Monsters
         */
        if(f instanceof Hero){
            String[] attackOptions = new String[victims.size()];
            int i =0;
            for (Figure v: victims){
                attackOptions[i++] = v.getName();
            }

            int indexChosen = UserInputs.showMenuAndGetUserAnswer(attackOptions);

            if (indexChosen < 0) return false;

            Monster target = (Monster)victims.get(indexChosen);

            // Choose weapon or spell
            DamageDealing tool = Battle.heroChooseAttackMedium((Hero)f);

            Battle.dealDamageWithTool(tool, (Hero)f, target);

            // remove all dead heroes
            Iterator<Monster> it = monsters.getMembers().iterator();

            while (it.hasNext()) {
                Monster m = it.next();
                if (!m.isAlive()) {
                    it.remove();
                    figureOriginalLanes.remove(m);
                    board.removePieceFromSquare(m);
                }
            }
        }
        else if (f instanceof  Monster){
            List<Integer> availableIndexes = IntStream.range(0, victims.size())
                    .boxed()
                    .collect(Collectors.toList());
            int victimIndex = ((Monster)f).getMonsterType().getStrategy().getVictimPartyIndex(victims, availableIndexes);
            Hero victim = (Hero)victims.get(victimIndex);

            int damageDealt = Battle.calculateNetDamageDoneOnHero((Monster)f, victim);
            Battle.dealDamage(f, victim, null, damageDealt);

            // remove all dead heroes
            Iterator<Hero> it = heroes.getMembers().iterator();

            while (it.hasNext()) {
                Hero h = it.next();
                if (!h.isAlive()) {
                    it.remove();
                    board.removePieceFromSquare(h);
                    toRespawn.addMember(h, false);
                }
            }
        }
        else{
            throw new IllegalArgumentException("Illegal Figure subclass parameter");
        }



        return true;
    }

    private boolean removeObstacle(LinkedHashMap<LVSquare, Integer[]> obstaclesInRange){

        String[] options = new String[obstaclesInRange.size()];
        int i = 0;

        ArrayList<Integer[]> indices = new ArrayList(obstaclesInRange.values());
        for(LVSquare sq: obstaclesInRange.keySet()){
            String s = String.format("(%d, %d)", obstaclesInRange.get(sq)[0], obstaclesInRange.get(sq)[1]);
            options[i++] = s;
        }

        int indexChosen = UserInputs.showMenuAndGetUserAnswer(options);

        if(indexChosen < 0) return false;
        board.removeObstacle(indices.get(indexChosen)[0], indices.get(indexChosen)[1]);
        return true;
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

        int[] pos = board.getMonsterPosition(m);
        if (pos != null) {
            ConsoleColors.printInColor(ConsoleColors.CYAN,
                    "Monster: " + m.getName() +
                            " at (" + pos[0] + ", " + pos[1] + ")");
        }

        boolean moved = board.moveMonsterForward(m);


        if (moved) {
            ConsoleColors.printInColor(ConsoleColors.GREEN,
                    "Monster advances toward heroes' Nexus.");
        }
        // monster couldn't move, try attack
        else{
            ArrayList<Hero> heroesInRange = board.getAllFiguresInAttackRange(pos[0], pos[1], Hero.class);

            if(heroesInRange.size() > 0){
                attack(m, heroesInRange);

            }else{
                ConsoleColors.printInColor(ConsoleColors.YELLOW,
                        "Monster stays in place (blocked or at edge).");
            }
        }

        m.decrementTimeOnAllEffects();
    }
}
