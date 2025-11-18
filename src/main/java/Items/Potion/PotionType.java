package Items.Potion;

import Figures.TraitType;

public enum PotionType {


    MP(null, "HP"),
    HP(null, "MP"),
//    trait types
    STRENGTH(TraitType.STRENGTH, "Strength"),
    DEXTERITY(TraitType.DEXTERITY, "Dexterity"),
    AGILITY(TraitType.AGILITY, "Agility");

    final public static int POTION_DURATION_LENGTH =5;
    private TraitType type;
    private String name;

    PotionType(TraitType t, String name){
        this.type = t;
        this.name = name;
    }

    public TraitType getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
