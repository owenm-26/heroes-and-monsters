package Items;

import UI.ConsoleColors;

public abstract class Item implements Equipable{
    protected String name;
    protected int level;
    protected int price; //(selling value /= 2, repairPrice /= 2)

    protected ItemType type;
    protected int usesLeft; //aka durability

    protected abstract String printOutOfUsesMessage();

    public boolean useItem(){
        /*
        Decrements uses left and returns True if the item is out of uses
         */
        usesLeft--;
        if(usesLeft == 0){
            ConsoleColors.printInColor(ConsoleColors.RED, printOutOfUsesMessage());
            return true;
        }
        return false;
    }

//    GETTERS
    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public ItemType getItemType() {
        return type;
    }

    public int getUsesLeft() {
        return usesLeft;
    }
}
