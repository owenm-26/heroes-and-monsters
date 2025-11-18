package Items;

import UI.ConsoleColors;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

public class Weapon extends Item{
    private int damage;
    private int handsRequired;

    public Weapon(String name, int level, int price, int usesLeft, int damage, int handsRequired){
        this.name=name;
        this.level=level;
        this.price = price;
        this.usesLeft=usesLeft;
        this.type=ItemType.WEAPON;
        this.damage = damage;
        this.handsRequired = handsRequired;
    }

    public boolean isTwoHanded(){
        if (handsRequired > 2){
            throw new ValueException("Hands Required is greater than 2. Are you sure this should be possible?");
        }
        return handsRequired == 2;
    }

    public int getDamage() {
        return damage;
    }

    @Override
    protected void printOutOfUsesMessage() {
        ConsoleColors.printInColor(ConsoleColors.RED, String.format("Your %s broke from wear!", name));
    }
}
