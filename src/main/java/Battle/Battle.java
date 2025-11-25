package Battle;

import Figures.Hero.Hero;
import Figures.Monster.Monster;
import Figures.Party;
import UI.ConsoleColors;

import java.util.ArrayList;
import java.util.List;

public class Battle {

    private Party<Hero> heroes;
    private Party<Monster> monsters;

    private List<Integer> heroesLeftIndices;
    private List<Integer> monstersLeftIndices;
    private int turnNumber; // odd is heroes turn, even is monsters

    public Battle(Party<Hero> heroes){
        this.heroes = heroes;
        heroesLeftIndices = new ArrayList<>(getIntegerRange(heroes.size()));
        monsters = Monster.assembleMonsterBattleParty(heroes);
        monstersLeftIndices = new ArrayList<>(getIntegerRange(monsters.size()));
        turnNumber = 1;
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

    private void executeTurn(){
        /*
        Handles a single turn of either the monsters or heroes
         */
        if(turnNumber % 2 == 1) executeHeroesTurn();
        else executeMonstersTurn();
    }

    private void executeHeroesTurn(){
        /*
        Iterates through all alive heroes in the party and gives them a choice of actions
         */
    }

    private void executeMonstersTurn(){
        /*
        Iterates through all alive monsters in the party and they take action based on their given strategy
         */
    }
}
