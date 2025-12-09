package Common.Figures.Hero;

import Common.Figures.TraitType;

public enum HeroType {
    WARRIOR("Warrior", 10, 5, 0),
    SORCERER("Sorcerer", 0, 5, 10),
    PALADIN("Paladin", 10, 0 ,5);

    private String name;
    private int strengthBonus;
    private int agilityBonus;
    private int dexterityBonus;

    HeroType(String name, int strengthBonus, int agilityBonus, int dexterityBonus){
        this.name = name;
        this.strengthBonus = strengthBonus;
        this.agilityBonus = agilityBonus;
        this.dexterityBonus = dexterityBonus;
    }

    public int getStrengthBonus() {
        return strengthBonus;
    }

    public int getAgilityBonus() {
        return agilityBonus;
    }

    public int getDexterityBonus() {
        return dexterityBonus;
    }

    public String getName() {
        return name;
    }

    public TraitType getFavoredTrait(){
        TraitType[] types = new TraitType[]{TraitType.STRENGTH, TraitType.AGILITY, TraitType.DEXTERITY};
        int[] values = new int[]{strengthBonus, agilityBonus, dexterityBonus};
        int favIndex = 0;
        for (int i=0; i < values.length; i++){
            if (values[i] > values[favIndex]) favIndex = i;
        }
        return types[favIndex];
    }
}
