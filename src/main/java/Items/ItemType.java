package Items;

import Items.Potion.PotionType;

public enum ItemType {

    WEAPON(),
    SPELL(),
    POTION(),
    ARMOR();

    public static ItemType fromName(String n) {
        for (ItemType a : values()) {
            if (a.toString().equalsIgnoreCase(n))
                return a;
        }
        throw new IllegalArgumentException("Invalid ItemType: " + n);
    }
}
