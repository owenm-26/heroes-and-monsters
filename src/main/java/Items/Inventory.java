package Items;

import Items.Potion.Potion;
import Items.Spell.Spell;
import UI.ConsoleColors;

import java.util.*;

import static UI.UserInputs.showMenuAndGetUserAnswer;

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

    private Map<String, List<? extends Item>> getInventoryCategories(){
        HashMap<String, List<? extends Item>> subSections = new HashMap<>();
        subSections.put("Weapons", weapons);
        subSections.put("Armor", armor);
        subSections.put("Spells", spells);
        subSections.put("Potions", potions);
        return subSections;
    }

    public List<? extends Item> selectInventorySubsection(){
        /*
        Presents a menu for the user to pick which Item inventory to look at and returns it
         */
        Map<String, List<? extends Item>> subSections = getInventoryCategories();
        String[] options = subSections.keySet().toArray(new String[0]);
        int indexChosen = showMenuAndGetUserAnswer(options);

        return subSections.get(options[indexChosen]);
    }

    public Map<String, ? extends Item> getSubInventoryOptions(List<? extends Item> subInventory){
        /*
        Returns the subInventory in a format to be used to create a menu to display
         */
        Map<String, Item> map = new LinkedHashMap<>();

        for (Item item : subInventory) {

            String key = item.getItemDescriptionOneLiner();

            // Handle duplicate names to avoid overwriting
            if (map.containsKey(key)) {
                int i = 2;
                String newKey = key + " (" + i + ")";
                while (map.containsKey(newKey)) {
                    i++;
                    newKey = key + " (" + i + ")";
                }
                key = newKey;
            }

            map.put(key, item);
        }

        return map;
    }
}
