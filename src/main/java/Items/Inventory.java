package Items;

import Items.Potion.Potion;
import Items.Spell.Spell;
import UI.ConsoleColors;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private List<Weapon> weapons;
    private List<Armor> armor;
    private List<Spell> spells;
    private List<Potion> potions;

    public Inventory(){
        weapons = new ArrayList<>();
        armor = new ArrayList<>();
        spells = new ArrayList<>();
        potions = new ArrayList<>();
    }
    public static Inventory generateMarketInventory(){
        return new Inventory();
    }

    public static void tradeItem(Item i, Inventory owner, Inventory receiver){
        owner.removeItem(i);
        receiver.addItem(i);
        ConsoleColors.printInColor(ConsoleColors.GREEN, String.format("🤝 %s successfully traded", i.getName()));
    }

    private void removeItem(Item i){
        /*
        Internal Method that removes a specific item from the player's or market's inventory
         */
        switch( i.getItemType()){
            case ARMOR:
                armor.remove(i);
                break;
            case WEAPON:
                weapons.remove(i);
                break;
            case SPELL:
                spells.remove(i);
                break;
            case POTION:
                potions.remove(i);
                break;
            default:
                throw new IllegalArgumentException("Invalid Item passed in to be removed.");
        }
    }

    private void addItem(Item i){
         /*
        Internal Method that adds a specific item to the player's or market's inventory
         */
        switch( i.getItemType()){
            case ARMOR:
                armor.add((Armor)i);
                break;
            case WEAPON:
                weapons.add((Weapon)i);
                break;
            case SPELL:
                spells.add((Spell) i);
                break;
            case POTION:
                potions.add((Potion) i);
                break;
            default:
                throw new IllegalArgumentException("Invalid Item passed in to be removed.");
        }

    }

//    GETTERS
    public List<Weapon> getWeapons() {
        return weapons;
    }

    public List<Armor> getArmor() {
        return armor;
    }

    public List<Spell> getSpells() {
        return spells;
    }

    public List<Potion> getPotions() {
        return potions;
    }
}
