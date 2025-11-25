package Battle;

import Figures.Hero.Hero;
import Figures.Monster.Monster;
import Figures.Party;
import UI.ConsoleColors;

public class Battle {

    private Party<Hero> heroes;
    private Party<Monster> monsters;
    private int turnNumber; // odd is heroes turn, even is monsters

    public Battle(Party<Hero> heroes){
        this.heroes = heroes;
        monsters = Monster.assembleMonsterBattleParty(heroes);
        turnNumber = 1;
    }

    public boolean executeBattle(){
        /*
        Handles full lifecycle of battle and returns whether the heroes won
         */
        ConsoleColors.printInColor(ConsoleColors.BLUE_BACKGROUND, "A new battle has begun!");
        Party<Hero> heroesLeft = heroes;
        Party<Monster> monstersLeft = monsters;
        while(monstersLeft.size() > 0 && heroesLeft.size() > 0){
            executeTurn();

            // update figures alive
            heroesLeft = heroes.getFiguresWithHealthRemaining();
            monstersLeft = monsters.getFiguresWithHealthRemaining();

            turnNumber++;
        }

        return heroesLeft.size() > 0;
    }

    private void executeTurn(){
        /*
        Handles a single turn of either the monsters or heroes
         */
        if(turnNumber % 2 == 1) executeHeroesTurn();
        else executeMonstersTurn();
    }

    private void executeHeroesTurn(){

    }

    private void executeMonstersTurn(){

    }
}
