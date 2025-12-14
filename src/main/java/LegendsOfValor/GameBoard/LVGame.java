package LegendsOfValor.GameBoard;

import Common.Figures.Figure;
import Common.Figures.Hero.Hero;
import Common.Figures.Monster.Monster;
import Common.Figures.Party;
import Common.Gameboard.Game;
import LegendsOfValor.GameBoard.LVSquare.LVSquare;
import Utility.UI.CommandType;
import Utility.UI.ConsoleColors;
import Utility.UI.LVMenuOptions;
import Utility.UI.UserInputs;

import java.util.*;

public class LVGame extends Game<LVBoard> {

    private Party<Hero> heroes;

    private Map<Figure, Integer> figureOriginalLanes;
    private List<Monster> monsters;

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
        monsters = new ArrayList<>();
        List<Monster> pool = Monster.getAllMonsterOptions();

        // Pick 3 random monsters
        for (int i = 0; i < 3; i++) {
            Monster m = pool.get((int) (Math.random() * pool.size()));
            monsters.add(m);
        }

        int row = 0;                // top Nexus
        int[] cols = {1, 4, 7};     // columns for lane 0,1,2 monsters

        for (int i = 0; i < 3; i++) {
            board.placeMonster(monsters.get(i), row, cols[i]);
            figureOriginalLanes.put(monsters.get(i), (i+1));
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
        for (Monster m: monsters){
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
            presentOptionsAndUndertakeUserChoice(pos);

            String input = UserInputs.parseAndQuitIfAsked();

            // MOVE: up/down/left/right (using CommandType mapping)
            if (UserInputs.isMovement(input)){
                actionTaken = attemptMove(h, input);
            }
            // TELEPORT
            else if (input.equalsIgnoreCase("t")) {
                actionTaken = attemptTeleport(h);
            }
            // RECALL
            else if (input.equalsIgnoreCase("r")) {
                actionTaken = attemptRecall(h);
            }
            // PASS
            else if (input.equalsIgnoreCase("p")) {
                ConsoleColors.printInColor(ConsoleColors.YELLOW,
                        "Hero passes the turn.");
                actionTaken = true;
            }
            else {
                ConsoleColors.printInColor(ConsoleColors.RED,
                        "Invalid command. Please try again.");
            }

            // Re-display board after a successful action
            if (actionTaken) {
                board.displayBoard();
            }
        }
    }

    private void presentOptionsAndUndertakeUserChoice(int[] position) {

        ArrayList<String> options = new ArrayList<>(Arrays.asList(LVMenuOptions.MOVE.getCode(), LVMenuOptions.BACKPACK.getCode(), LVMenuOptions.STATS.getCode()));

        // check if on Nexus
        if (board.getSquareInventory(position[0], position[1]) != null){
            options.add(LVMenuOptions.MARKET.getCode());
        }
        // gather all monsters around and all obstacles around
        ArrayList<Monster> monstersInRange = board.getAllMonstersInAttackRange(position[0], position[1]);
        ArrayList<LVSquare> obstaclesInRange = board.getAllObstaclesInRange(position[0], position[1]);

        if (monstersInRange.size() > 0) options.add(LVMenuOptions.ATTACK.getCode());
        if (obstaclesInRange.size() > 0) options.add(LVMenuOptions.REMOVE_OBSTACLE.getCode());

        boolean turnUsed = false;

        while (!turnUsed){
            int index = -1;
            while(index < 0){
                index = UserInputs.showMenuAndGetUserAnswer(options.toArray(new String[0]));
                if (index < 0) ConsoleColors.printInColor(ConsoleColors.RED ,"Cannot go back in this menu");
            }

            //Handle Choice
            String choice = options.get(index);
            switch (LVMenuOptions.fromName((choice))){
                case MOVE:
                    //TODO: Implement Movement helper
                    turnUsed = true;
                    break;
                case BACKPACK:
                    //TODO: Implement Backpack helper
                    break;
                case STATS:
                    //TODO: Implement Stats helper
                    break;
                case MARKET:
                    //TODO: Implement Market helper
                    break;
                case REMOVE_OBSTACLE:
                    //TODO: Implement Remove Obstacle helper
                    turnUsed = true;
                    break;
                case ATTACK:
                    //TODO: Implement Attack helper
                    turnUsed = true;
                    break;
            }
        }


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
