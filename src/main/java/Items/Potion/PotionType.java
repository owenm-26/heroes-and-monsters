package Items.Potion;

import Figures.TraitType;
import GameBoard.HMSquare.MarketActions;

public enum PotionType {

    MP(null, "Mana"),
    HP(null, "Health"),
    DEFENSE(null, "Defense"),
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

    public static PotionType fromName(String n) {
        for (PotionType a : values()) {
            if (a.name.equalsIgnoreCase(n))
                return a;
        }
        throw new IllegalArgumentException("Invalid PotionType: " + n);
    }

    public TraitType getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
