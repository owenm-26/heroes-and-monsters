package Figures.Monster;

import Figures.Figure;
import Figures.Hero.HeroType;

public class Monster extends Figure {
    private int baseDamage;
    private int baseDefense;
    private int baseDodge;
    private MonsterType type;

    public Monster(String name, int baseDamage, int baseDefense, int baseDodge, MonsterType type){
        this.name = name;
        this.baseDamage = baseDamage + type.getBaseDamageBonus();
        this.baseDefense = baseDefense + type.getBaseDefenseBonus();
        this.baseDodge = baseDodge + type.getBaseAgilityBonus();
        this.type = type;
    }
}
