package Figures.Monster;

import Figures.Monster.Strategies.LeastHealthTarget;
import Figures.Monster.Strategies.LowestLevelTarget;
import Figures.Monster.Strategies.MonsterAttackStrategy;
import Figures.Monster.Strategies.RandomTarget;

public enum MonsterType {
    DRAGON("Dragon", new RandomTarget(),10, 0, 0),
    EXOSKELETON("Exoskeleton", new LowestLevelTarget(), 0, 10, 0),
    SPIRIT("Spirit", new LeastHealthTarget(),0,0,10);

    private String name;
    private int baseDamageBonus;
    private int baseDefenseBonus;
    private int baseAgilityBonus;
    private MonsterAttackStrategy strategy;

    MonsterType(String name, MonsterAttackStrategy strategy, int baseDamageBonus, int baseDefenseBonus, int baseAgilityBonus){
        this.name = name;
        this.strategy = strategy;
        this.baseDamageBonus = baseDamageBonus;
        this.baseAgilityBonus = baseAgilityBonus;
        this.baseDefenseBonus = baseDefenseBonus;
    }

    public String getName() {
        return name;
    }

    public int getBaseAgilityBonus() {
        return baseAgilityBonus;
    }

    public int getBaseDamageBonus() {
        return baseDamageBonus;
    }

    public int getBaseDefenseBonus() {
        return baseDefenseBonus;
    }

    public MonsterAttackStrategy getStrategy() {
        return strategy;
    }
}
