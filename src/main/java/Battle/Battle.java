package Battle;

import Figures.Figure;
import Figures.Hero.Hero;
import Figures.Monster.Monster;
import Figures.Party;
import GameBoard.HMGame;
import Items.DamageDealing;
import Items.Item;
import Items.ItemType;
import UI.ConsoleColors;
import UI.GeneralPrints;
import UI.UserInputs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Battle.BattleCommand.fromName;

public class Battle {

    private Party<Hero> heroes;
    private Party<Monster> monsters;

    private List<Integer> heroesLeftIndices;
    private List<Integer> monstersLeftIndices;
    private int turnNumber; // odd is heroes turn, even is monsters

    private HMGame game;

    public Battle(Party<Hero> heroes, HMGame g){
        this.heroes = heroes;
        heroesLeftIndices = new ArrayList<>(getIntegerRange(heroes.size()));
        monsters = Monster.assembleMonsterBattleParty(heroes);
        monstersLeftIndices = new ArrayList<>(getIntegerRange(monsters.size()));
        turnNumber = 1;
        game = g;
    }

    private List<Integer> getIntegerRange(int upperBound){
        /*
        Returns a list of integers from 0 to upperBound (not inclusive)
         */
        List<Integer> range = new ArrayList<>();
        for(int i =0;i < upperBound; i++){
            range.add(i);
        }
        return range;
    }

    public boolean executeBattle(){
        /*
        Handles full lifecycle of battle and returns whether the heroes won
         */
        ConsoleColors.printInColor(ConsoleColors.BLUE_BACKGROUND, "A new battle has begun!");
        while(monstersLeftIndices.size() > 0 && heroesLeftIndices.size() > 0){
            executeTurn();

            // update figures alive
            heroesLeftIndices = heroes.getFigureIndexesWithHealthRemaining();
            monstersLeftIndices = monsters.getFigureIndexesWithHealthRemaining();

            turnNumber++;
        }

        return heroesLeftIndices.size() > 0;
    }

    private boolean isHeroTurn(){
        return turnNumber % 2 == 1;
    }

    private void executeTurn(){
        /*
        Handles a single turn of either the monsters or heroes
         */
        if(isHeroTurn()) executeHeroesTurn();
        else executeMonstersTurn();
    }

    private void executeHeroesTurn(){
        /*
        Iterates through all alive heroes in the party and gives them a choice of actions
         */
        GeneralPrints.printHorizontalLine();

        ConsoleColors.printInColor(ConsoleColors.YELLOW_BOLD, String.format("TURN #%d: HEROES\n", turnNumber));
        List<Hero> heroList = heroes.getMembers();
        for(int index: heroesLeftIndices){
            Hero curr = heroList.get(index);
            ConsoleColors.printInColor(ConsoleColors.BLUE_BOLD, String.format("🦸 It is %s's turn to fight!", curr.getName()));
            presentHeroActionsMenu(curr);
        }

    }

    private void presentHeroActionsMenu(Hero h){
        /*
        Shows the user a menu to
        - attack with a weapon
        - cast a spell
        - take a potion
        - equip a weapon or armor
        - or see a monster / hero's stats
         */
        int optionIndexChosen = -1;
        String[] options = {};
        while (optionIndexChosen < 0){
            options = BattleCommand.getAllActions();
            optionIndexChosen = UserInputs.showMenuAndGetUserAnswer(options, true, game);
            if(game.isViewingStatistics()){
                game.viewStats();
            }
        }
        switch (BattleCommand.fromName(options[optionIndexChosen])){
            case CHANGE_WEAPON:
                h.equipItem(ItemType.WEAPON);
                break;
            case CHANGE_ARMOR:
                h.equipItem(ItemType.ARMOR);
                break;
            case TAKE_POTION:
                h.equipItem(ItemType.POTION);
            case ATTACK:
                heroAttack(h);
        }
    }

    private void executeMonstersTurn(){
        /*
        Iterates through all alive monsters in the party and they take action based on their given strategy
         */

        GeneralPrints.printHorizontalLine();

        ConsoleColors.printInColor(ConsoleColors.YELLOW_BOLD, String.format("TURN #%d: MONSTERS\n", turnNumber));
        List<Monster> monsterList = monsters.getMembers();
        for(int index: monstersLeftIndices){
            Monster curr = monsterList.get(index);
            ConsoleColors.printInColor(ConsoleColors.BLUE_BOLD, String.format("🧌 It is %s's turn to fight!", curr.getName()));

        }
    }

    private void printAttackResult(Figure attacker, Figure receiver, DamageDealing tool, int damageDealt){
        String symbol = tool==null ? "👊" : tool.getItemType() == ItemType.WEAPON ? "🥊" : "🪄";
        String toolName = tool==null ? "Fists" : tool.getName();
        ConsoleColors.printInColor(ConsoleColors.RED_BACKGROUND, String.format("%s %s hit %s with %s for %d damage!", symbol, attacker.getName(), receiver.getName(), toolName, damageDealt));
    }

    private void dealDamage(Figure attacker, Figure receiver, DamageDealing tool, int damageDealt){
        if(tool != null){
            boolean outOfUses = tool.useItem();
            if (outOfUses && isHeroTurn()){
                Hero h = (Hero)attacker;
                h.getInventory().removeItem((Item)tool);
            }
        }
        receiver.loseHp(damageDealt);
        if (!receiver.isAlive()) ConsoleColors.printInColor(ConsoleColors.RED_BOLD, String.format("%s has been defeated and knocked out of the battle!", receiver.getName()));
        printAttackResult(attacker, receiver, tool, damageDealt);
    }

    private void heroAttack(Hero h){
        /*
        Allows Hero to choose from their attacking items and then allows them to pick which foe and then attacks
         */

        // What type of tool?
        String[] options = {ItemType.WEAPON.toString(), ItemType.SPELL.toString()};
        int optionChosenIndex = UserInputs.showMenuAndGetUserAnswer(options);
        if (optionChosenIndex < 0) return;
        ItemType t = ItemType.fromName(options[optionChosenIndex]);

        // Select from that type
        DamageDealing tool = (DamageDealing)h.selectItem(t);

        //Select opponent
        Map<String, Figure> eligibleTargets = getEligibleTargets(monstersLeftIndices, monsters.getMembers());
        String[] monsterOptions = eligibleTargets.keySet().toArray(new String[0]);
        int targetChosenIndex = UserInputs.showMenuAndGetUserAnswer(monsterOptions);
        if (targetChosenIndex < 0) return;
        Monster foe = (Monster)eligibleTargets.get(monsterOptions[targetChosenIndex]);

        // damage dealt
        int damage = tool != null ? calculateNetDamageDoneOnMonster(h, foe, tool) : h.getPunchDamage();
        dealDamage(h, foe, tool, damage);
    }

    private static Map<String, Figure> getEligibleTargets(List<Integer> indices, List<? extends Figure> members){
        Map<String, Figure> eligible = new HashMap<>();
        for(int i: indices){
            Figure m = members.get(i);
            eligible.put(m.getName(), m);
        }
        return eligible;
    }

    public Party<Monster> getMonsters() {
        return monsters;
    }

    private int calculateNetDamageDoneOnMonster(Hero h, Monster m, DamageDealing tool){
        if (m.dodgedSuccessfully()) {
            ConsoleColors.printInColor(ConsoleColors.RED_BACKGROUND, String.format("Uh oh, %s dodged your attack!", m.getName()));
            return 0;
        }
        int toolDamage = tool.getDamageDealt(h.getStrength());
        System.out.println(toolDamage + " - "+  m.getBaseDefense());
        return Math.max((tool.getDamageDealt(h.getStrength()) - m.getBaseDefense()), 0);
    }
}
