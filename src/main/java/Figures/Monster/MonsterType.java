package Figures.Monster;

public enum MonsterType {
    DRAGON("Dragon", 10, 0, 0),
    EXOSKELETON("Exoskeleton", 0, 10, 0),
    SPIRIT("Spirit",0,0,10);

    private String name;
    private int baseDamageBonus;
    private int baseDefenseBonus;
    private int baseAgilityBonus;

    MonsterType(String name, int baseDamageBonus, int baseDefenseBonus, int baseAgilityBonus){
        this.name = name;
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
}
