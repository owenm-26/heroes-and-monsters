package Figures.Monster;

import Figures.Figure;
import Figures.Hero.HeroType;
import GameBoard.HMGameState;

public class Monster extends Figure {
    private int baseDamage;
    private int baseDodge;
    private MonsterType type;

    public Monster(String name, int baseDamage, int baseDefense, int baseDodge, MonsterType type){
        this.name = name;
        this.baseDamage = baseDamage + type.getBaseDamageBonus();
        this.baseDefense = baseDefense + type.getBaseDefenseBonus();
        this.baseDodge = baseDodge + type.getBaseAgilityBonus();
        this.type = type;
    }

    public void displayFigureStatistics(HMGameState state){
        /*
        Displays statistics of figure based on the game state
         */
    }
}
