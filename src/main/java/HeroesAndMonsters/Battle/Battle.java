package HeroesAndMonsters.Battle;
/*
FILE HEADER:
Has instance and static methods to unify battle logic between heroes and monsters. can have all v all battles
 */

import Common.Figures.Figure;
import Common.Figures.Party;
import Common.Figures.Hero.Hero;
import Common.Figures.Monster.Monster;
import Common.Items.DamageDealing;
import Common.Items.Item;
import Common.Items.ItemType;
import Common.Items.Spell.Spell;
import HeroesAndMonsters.GameBoard.HMGame;
import Utility.UI.ConsoleColors;
import Utility.UI.GeneralPrints;
import Utility.UI.UserInputs;

import java.util.*;

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
        System.out.format("HEROES REMAINING: %d\n", heroesLeftIndices.size());
        System.out.format("MONSTERS REMAINING: %d\n", monstersLeftIndices.size());
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
            // dont execute if the battle is over
            if(monstersLeftIndices.size() == 0) break;

            Hero curr = heroList.get(index);
            ConsoleColors.printInColor(ConsoleColors.BLUE_BOLD, String.format("\n🦸 It is %s's turn to fight!", curr.getName()));
            presentHeroActionsMenu(curr);

            // update figures alive
            heroesLeftIndices = heroes.getFigureIndexesWithHealthRemaining();
            monstersLeftIndices = monsters.getFigureIndexesWithHealthRemaining();

            //decrement effects
            curr.decrementTimeOnAllEffects();
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
        boolean usedTurn = false;

        switch (BattleCommand.fromName(options[optionIndexChosen])){
            case CHANGE_WEAPON:
                usedTurn = h.equipItem(ItemType.WEAPON);
                break;
            case CHANGE_ARMOR:
                usedTurn =h.equipItem(ItemType.ARMOR);
                break;
            case TAKE_POTION:
                usedTurn = h.equipItem(ItemType.POTION);
                break;
            case ATTACK:
                heroAttack(h);
                usedTurn = true;
                break;
        }
        if (!usedTurn) presentHeroActionsMenu(h);

    }

    private void executeMonstersTurn(){
        /*
        Iterates through all alive monsters in the party and they take action based on their given strategy
         */

        GeneralPrints.printHorizontalLine();

        ConsoleColors.printInColor(ConsoleColors.YELLOW_BOLD, String.format("TURN #%d: MONSTERS\n", turnNumber));
        List<Monster> monsterList = monsters.getMembers();
        for(int index: monstersLeftIndices){

            // update figures alive
            heroesLeftIndices = heroes.getFigureIndexesWithHealthRemaining();
            monstersLeftIndices = monsters.getFigureIndexesWithHealthRemaining();

            // dont execute if the battle is over
            if(heroesLeftIndices.size() == 0) break;

            Monster curr = monsterList.get(index);
            ConsoleColors.printInColor(ConsoleColors.BLUE_BOLD, String.format("\n🧌 It is %s's turn to fight!", curr.getName()));

            // pick what hero to attack based on monster strategy
            int victimIndex = curr.getMonsterType().getStrategy().getVictimPartyIndex(heroes.getMembers(), heroesLeftIndices);
            Hero victim = heroes.getMembers().get(victimIndex);

            // attack them
            int damageDealt = calculateNetDamageDoneOnHero(curr, victim);
            dealDamage(curr, victim, null, damageDealt);

            //decrement effects
            curr.decrementTimeOnAllEffects();
        }
    }

    private static void printAttackResult(Figure attacker, Figure receiver, DamageDealing tool, int damageDealt){
        String symbol = tool==null ? "👊" : tool.getItemType() == ItemType.WEAPON ? "🥊" : "🪄";
        String toolName = tool==null ? "Fists" : tool.getName();
        ConsoleColors.printInColor(ConsoleColors.RED_BACKGROUND, String.format("%s %s hit %s with %s for %d damage!", symbol, attacker.getName(), receiver.getName(), toolName, damageDealt));
    }

    public static void dealDamage(Figure attacker, Figure receiver, DamageDealing tool, int damageDealt){
        if(tool != null){
            boolean outOfUses = tool.useItem();
            if (outOfUses && attacker instanceof Hero){
                Hero h = (Hero)attacker;
                h.getInventory().removeItem((Item)tool);
            }
        }
        receiver.loseHp(damageDealt);
        if (!receiver.isAlive()) ConsoleColors.printInColor(ConsoleColors.RED_BOLD, String.format("%s has been defeated and knocked out of the battle!", receiver.getName()));
        printAttackResult(attacker, receiver, tool, damageDealt);
    }

    public static DamageDealing heroChooseAttackMedium(Hero h){
        // What type of tool?
        //Check if there are any valid ones, if so ask which they choose
        String[] toolOptions = new String[2];
        int i =0;
        if(h.getWeaponsEquipped().size() != 0) toolOptions[i++] = ItemType.WEAPON.toString();

        ArrayList<Spell> spellsAvailable = new ArrayList((h.getNonEquippedItemsOfSubCategory(ItemType.SPELL)).values());
        if(h.spellsHeroCanAffordToCast(spellsAvailable).size() != 0) toolOptions[i] = ItemType.SPELL.toString(); // add spell if they have any

        DamageDealing tool = null;
        ItemType t = null;
        if (toolOptions[0] != null){
            int optionChosenIndex = -1;
            while(optionChosenIndex < 0){
                optionChosenIndex = UserInputs.showMenuAndGetUserAnswer(toolOptions);
                if (optionChosenIndex < 0) ConsoleColors.printInColor(ConsoleColors.RED, "Cannot go back in this screen. Please select one.");
            }
            t = ItemType.fromName(toolOptions[optionChosenIndex]);

            // Select from that type
            tool = (DamageDealing)h.selectItem(t);
        }

        return tool;
    }

    public static void dealDamageWithTool(DamageDealing tool, Hero h, Monster m){
        int damage = tool != null ? calculateNetDamageDoneOnMonster(h, m, tool) : h.getPunchDamage();
        dealDamage(h, m, tool, damage);
        if (tool!= null && tool.getItemType()==ItemType.SPELL) h.useMana(((Spell)tool).getMpCost());
    }

    private void heroAttack(Hero h){
        /*
        Allows Hero to choose from their attacking items and then allows them to pick which foe and then attacks
         */

        DamageDealing tool = heroChooseAttackMedium(h);

        //Select opponent
        Map<String, Figure> eligibleTargets = getEligibleTargets(monstersLeftIndices, monsters.getMembers());
        String[] monsterOptions = eligibleTargets.keySet().toArray(new String[0]);
        int targetChosenIndex = UserInputs.showMenuAndGetUserAnswer(monsterOptions);
        if (targetChosenIndex < 0) return;
        Monster foe = (Monster)eligibleTargets.get(monsterOptions[targetChosenIndex]);

        // damage dealt
        int damage = tool != null ? calculateNetDamageDoneOnMonster(h, foe, tool) : h.getPunchDamage();
        dealDamage(h, foe, tool, damage);
        if (tool!= null && tool.getItemType()==ItemType.SPELL) h.useMana(((Spell)tool).getMpCost());
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

    private static int calculateNetDamageDoneOnMonster(Hero h, Monster m, DamageDealing tool){
        if (m.dodgedSuccessfully()) {
            ConsoleColors.printInColor(ConsoleColors.RED_BACKGROUND, String.format("Uh oh, %s dodged your attack!", m.getName()));
            return 0;
        }

        return Math.max((tool.getDamageDealt(h.getStrength()) - m.getDamageBlocked()), 0);
    }

    public static int calculateNetDamageDoneOnHero(Monster m, Hero h){
        if (h.dodgedSuccessfully()){
            ConsoleColors.printInColor(ConsoleColors.RED_BACKGROUND, String.format("Nice, %s dodged %s's attack!", h.getName(), m.getName()));
            return 0;
        }

        return Math.max(0, m.getPunchDamage());
    }

    public void handleHeroWinAndRewards(){
        /*
        Handles all printing and gold / xp awarding & dead hero reviving
         */
        GeneralPrints.printDoubleThickHorizontalLine();
        ConsoleColors.printInColor(ConsoleColors.YELLOW_BOLD, String.format("🎉 Heroes have won the battle! Each alive hero reaps the following rewards and the fainted heroes are revived at half health and mana."));

        int goldEarned = 0;
        int xpEarned = 0;

        for (Monster m: monsters.getMembers()){
            goldEarned += m.getLevel() * 100;
            xpEarned += m.getLevel() * 2;
        }

        ConsoleColors.printInColor(ConsoleColors.YELLOW_BOLD, String.format("  -💰+%d gold\n  -📚 +%d XP", goldEarned, xpEarned));

        for(int i=0; i < heroes.size(); i++){
            Hero h = heroes.getMembers().get(i);

            // Dead heroes get no rewards but are revived with half stats
            if (!heroesLeftIndices.contains(i)) h.reviveAfterBattle();
            else h.rewardHero(goldEarned, xpEarned);
        }
    }
}
