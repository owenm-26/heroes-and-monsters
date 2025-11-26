package Figures.Hero;

import Data.LoadableFromText;
import Data.TextDataLoader;
import Figures.Figure;
import GameBoard.HMEffect;
import GameBoard.HMGameState;
import Items.*;
import Items.Potion.Potion;
import Items.Potion.PotionType;
import UI.ConsoleColors;
import UI.GeneralPrints;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.util.*;

import static Data.TextDataLoader.getAllSourceFileNames;
import static Items.Inventory.tradeItem;
import static Items.Potion.PotionType.POTION_DURATION_LENGTH;
import static UI.ConsoleColors.*;
import static UI.UserInputs.showMenuAndGetUserAnswer;
import static Validators.Integers.validatePositiveIntegers;

public class Hero extends Figure implements LoadableFromText {
    private int xp;
    private int mp;
    private int mpMax;
    private int gold;
    private List<Armor> armorWorn;
    private List<Weapon> weaponsEquipped;
    private int numberOfHands;
    private HeroType heroType;

    private Inventory inventory;

    // attributes
    private int agility;
    private int strength;
    private int dexterity;

    public Hero(){
        //equipment
        initializeEmptyEquipment();
        xp= 0;
        baseDefense = 0;
        level = 1;
        numberOfHands = 2;
    }

    @Override
    public boolean dodgedSuccessfully() {
        return Math.random() > (agility * 0.002);
    }

    public void displayFigureStatistics(HMGameState state){
        final String COLOR = ConsoleColors.CYAN_BOLD;

        ConsoleColors.printInColor(COLOR, "===== HERO STATUS =====");

        ConsoleColors.printInColor(COLOR, "Name: " + name + " (" + heroType.getName() + ")");
        ConsoleColors.printInColor(COLOR, "Level: " + level);
        ConsoleColors.printInColor(COLOR, "HP: " + hp + "/" + hpMax);
        ConsoleColors.printInColor(COLOR, "MP: " + mp + "/" + mpMax);

        ConsoleColors.printInColor(COLOR, "--- Attributes ---");
        ConsoleColors.printInColor(COLOR, "Strength:  " + strength);
        ConsoleColors.printInColor(COLOR, "Dexterity: " + dexterity);
        ConsoleColors.printInColor(COLOR, "Agility:   " + agility);

        // When OUTSIDE of battle
        if (state != HMGameState.BATTLING) {
            ConsoleColors.printInColor(COLOR, "XP: " + xp);
            ConsoleColors.printInColor(COLOR, "Gold: " + gold);

        } else {
            // When IN battle
            ConsoleColors.printInColor(COLOR, "--- Equipped Weapons ---");

            if (weaponsEquipped == null || weaponsEquipped.isEmpty()) {
                ConsoleColors.printInColor(COLOR, "None");
            } else {
                for (Weapon w : weaponsEquipped) {
                    ConsoleColors.printInColor(COLOR, "- " + w.getName());
                }
            }

            ConsoleColors.printInColor(COLOR, "--- Armor Worn ---");

            if (armorWorn == null || armorWorn.isEmpty()) {
                ConsoleColors.printInColor(COLOR, "None");
            } else {
                for (Armor a : armorWorn) {
                    ConsoleColors.printInColor(COLOR, "- " + a.getName());
                }
            }
        }

        ConsoleColors.printInColor(COLOR, "========================\n");
    }

    @Override
    public int getPunchDamage() {
        return strength/10;
    }


    @Override
    public String toString() {

        StringBuilder b = new StringBuilder();

        // Top border
//        b.append(returnInColor(WHITE_BACKGROUND, GeneralPrints.returnDoubleThickHorizontalLine()));

        // NAME AND TITLE
        b.append(returnInColor(PURPLE_BOLD,
                "HERO: " + name + "   (" + heroType + ")"));

        // BASIC STATS
        b.append(returnInColor(CYAN_BOLD,
                "Level: " + level));
        b.append(returnInColor(GREEN_BOLD,
                "HP: " + hp + "/" + hpMax));
        b.append(returnInColor(BLUE_BOLD,
                "MP: " + mp + "/" + mpMax));
        b.append(returnInColor(YELLOW_BOLD,
                "XP: " + xp));
        b.append(returnInColor(RED_BOLD,
                "Gold: " + gold));

        // ATTRIBUTES
        b.append(returnInColor(WHITE_BOLD, "ATTRIBUTES"));
        b.append(returnInColor(GREEN,
                "Strength:  " + strength));
        b.append(returnInColor(BLUE,
                "Dexterity: " + dexterity));
        b.append(returnInColor(PURPLE,
                "Agility:   " + agility));

        // EQUIPMENT SECTIONS
        b.append(returnInColor(WHITE_BOLD, "EQUIPMENT"));

        // ----- WEAPONS -----
        b.append(returnInColor(YELLOW_BOLD, "Weapons Equipped:"));
        if (weaponsEquipped == null || weaponsEquipped.isEmpty()) {
            b.append(returnInColor(YELLOW, "  None"));
        } else {
            for (Weapon w : weaponsEquipped) {
                b.append(returnInColor(YELLOW, "  - " + w.getName()));
            }
        }

        // ----- ARMOR -----
        b.append(returnInColor(CYAN_BOLD, "Armor Worn:"));
        if (armorWorn == null || armorWorn.isEmpty()) {
            b.append(returnInColor(CYAN, "  None"));
        } else {
            for (Armor a : armorWorn) {
                b.append(returnInColor(CYAN, "  - " + a.getName()));
            }
        }

        // Bottom border
        b.append(returnInColor(WHITE_BACKGROUND, GeneralPrints.returnDoubleThickHorizontalLine(), false));

        return b.toString();
    }

    private void setMana(int mpMax){
        this.mpMax = mpMax;
        mp = mpMax;
    }

    private void initializeAttributes(int strength, int dexterity, int agility){
        /*
        Sets strength, dexterity, and agility
         */
        this.strength = strength + heroType.getStrengthBonus();
        this.dexterity = dexterity + heroType.getDexterityBonus();
        this.agility = agility + heroType.getAgilityBonus();

    }
    private void initializeEmptyEquipment(){
        weaponsEquipped = new ArrayList<>();
        armorWorn = new ArrayList<>();
        activeEffects = new HashSet<>();
        inventory = new Inventory();
    }

    private void setHeroTypeFromFileName(String fileName){
        if (fileName.length() == 0) throw new IllegalArgumentException("Filename is length 0 in setHeroTypeFromFileName()");
        switch (fileName){
            case "paladins":
                heroType = HeroType.PALADIN;
                break;
            case "sorcerers":
                heroType = HeroType.SORCERER;
                break;
            case "warriors":
                heroType = HeroType.WARRIOR;
                break;
            default:
                throw new ValueException("Something went wrong in setHeroTypeFromFileName()");
        }
    }

    public void loadFromMap(Map<String, String> map){
//        Name/mana/strength/agility/dexterity/starting money/starting experience
        this.name = map.get("Name");
        this.gold= Integer.parseInt(map.get("starting money"));
        int xp_og = Integer.parseInt(map.get("starting experience"));
        addXP(xp_og);

        // Hero Type
        setHeroTypeFromFileName(map.get("file name"));
        // Hero attributes
        initializeAttributes(Integer.parseInt(map.get("strength")), Integer.parseInt(map.get("dexterity")), Integer.parseInt(map.get("agility")));
        // Mana
        setMana(Integer.parseInt(map.get("mana")));
        // Health
        calculateHpMax();

        validatePositiveIntegers(gold, numberOfHands, xp_og, mp, strength, dexterity, agility);
    }

    public static List<Hero> getAllHeroOptions(){
        ArrayList<Hero> heroes = new ArrayList<>();
        try{
            for(String filename: getAllSourceFileNames("Heroes")){
                heroes.addAll(TextDataLoader.load("Heroes", filename, Hero.class));
            }
        }catch (Exception e){
            System.out.format("Failed to load all heroes: %s\n", e);
        }
//        System.out.format("Number of heroes gotten from the files: %d\n", heroes.size());
        return heroes;
    }

    public boolean canBuyItem(Item i){
        return i.getPrice() <= gold;
    }

//    SETTERS
    public void addXP(int p){
        /*
        Handles the level up logic in the case where xp + p > xp_map
         */

        xp = xp + p;
        int xpMax = (level+1) * 100;
        if (xp > xpMax){
            level += 1;
            xp = xp - xpMax;
            ConsoleColors.printInColor(ConsoleColors.YELLOW_BOLD, String.format("%s has leveled up to level %d", name, level));
        }
    }

    public void gainMp(int p){
        mp = Math.min(mpMax, mp + p);
    }

    public void adjustGold(int g){
        /*
        To use when buying an item or selling an item or winning a battle
         */
        gold += g;
    }

    private void unequipItem(Item i){
        if (!i.isEquipable()) throw new IllegalArgumentException("This item is not equipable");
        
        if (i.getItemType() == ItemType.ARMOR){
            if (!armorWorn.contains(i)) throw new IllegalArgumentException("This armor is not equipped");
            armorWorn.remove(i);
        }
        else{
            if (!weaponsEquipped.contains(i)) throw new IllegalArgumentException("This weapon is not equipped");
            weaponsEquipped.remove(i);
        }
        ConsoleColors.printInColor(ConsoleColors.BLACK_BACKGROUND, String.format("%s has been unequipped.", i.getName()));
    }

    public Item selectItem(ItemType t){
        /*
        Returns item selected by user
         */
        Map<String, ? extends Item> map;
        switch (t){
            case WEAPON:
                map = inventory.getSubInventoryOptions(inventory.getWeapons());
                break;
            case ARMOR:
                map = inventory.getSubInventoryOptions(inventory.getArmor());
                break;
            case POTION:
                map = inventory.getSubInventoryOptions(inventory.getPotions());
                break;
            case SPELL:
                map = inventory.getSubInventoryOptions(inventory.getSpells());
                break;
            default:
                throw new IllegalArgumentException("Item Type given is not equipable");
        }

        String[] itemOptions = map.keySet().toArray(new String[0]);
        int itemToEquipIndex = showMenuAndGetUserAnswer(itemOptions);
        if (itemToEquipIndex < 0) return null;
        return map.get(itemOptions[itemToEquipIndex]);
    }

    public void equipItem(ItemType t){
        /*
        Public method that handles which equip and unequip logic to use
         */
        Item toEquip = selectItem(t);
        if (toEquip == null) return;

        if (toEquip.getItemType() == ItemType.WEAPON) equipWeapon((Weapon) toEquip);
        else if (toEquip.getItemType() == ItemType.ARMOR) equipArmor((Armor) toEquip);
        else usePotion((Potion) toEquip);
    }

    private void equipWeapon(Weapon w){
        /*
        Equips a new weapon and deequips as many weapons as needed
         */
//        CASE #1 & #2: Need two hands for a weapon
        if (w.isTwoHanded()){
            while (weaponsEquipped.size() + 2 > numberOfHands){
                unequipItem(weaponsEquipped.get(0));
            }
            weaponsEquipped.add(w);
        }
//        CASE #3: Need one hand and there's one available
        else if(!w.isTwoHanded() && weaponsEquipped.size() < 2){
            weaponsEquipped.add(w);
        }
//        CASE #4: Need one hand and there's not one available
        else{
            unequipItem(weaponsEquipped.get(0));
            weaponsEquipped.add(w);
        }

        printItemEquipped(w);
    }

    private void printItemEquipped(Item i){
        if (!i.isEquipable()) throw new IllegalArgumentException("This item is not equipable");
        String m = "";

        switch (i.getItemType()){
            case ARMOR:
                m = String.format("🛡️ %s armor equipped!", i.getName());
                break;
            case WEAPON:
                m = String.format("⚔️ %s weapon equipped!", i.getName());
                break;
            default:
                throw new IllegalArgumentException("Something went wrong in printItemEquipped");
        }
        ConsoleColors.printInColor(ConsoleColors.BLACK_BACKGROUND, m);
    }

    private void equipArmor(Armor a){
        for (Armor ar: armorWorn){
            unequipItem(ar);
        }
        armorWorn.add(a);
        printItemEquipped(a);
    }

    public void usePotion(Potion p){
        HashMap<PotionType, Integer> effects = p.getEffects();
        String message = String.format("🧪 Taking %s potion...", p.getName());
        ConsoleColors.printInColor(ConsoleColors.PURPLE_BOLD, message);
        for (PotionType type: effects.keySet()){
            triggerPotionEffect(p.getName(), type, effects.get(type));
        }
    }

    private void triggerPotionEffect(String potionName, PotionType type, int val){
        /*
        Internal method to apply a single effect of a potion
         */
        if (type == PotionType.HP){
            gainHp(val);
        }
        else if(type == PotionType.MP){
            gainMp(val);
        }
        else{
            String name = String.format("%s-%s", potionName, type.getName());
            HMEffect e = new HMEffect(name, val, POTION_DURATION_LENGTH, type.getType());
            activeEffects.add(e);
        }
        String message = String.format("- Gaining %s effect of %d", type.getName(), val);
        ConsoleColors.printInColor(ConsoleColors.PURPLE, message);
    }

    // Trading mechanics

    public void buyItem(Item i, Inventory merchant){
        /*
        Handles all validation, gold adjustment, and inventory swapping required
         */

        // eligible?
        if(this.level < i.getLevel()){
            ConsoleColors.printInColor(RED_BOLD, String.format("⚠️Hero %s's level is too low to buy this item: %d < %d", name, level, i.getLevel()));

        }
        else if(!canBuyItem(i)) {
            ConsoleColors.printInColor(RED_BOLD, String.format("⚠️Hero %s's gold is too low to buy this item: %d < %d", name, gold, i.getPrice()));
        }
        else{
            tradeItem(i, merchant, this.inventory);
            gold -= i.getPrice();
            i.setPrice(i.getPrice()/ 2);

        }

    }

    private boolean itemIsEquipped(Item i){
        /*
        Helper method that tells whether an item is equipped
         */
        switch (i.getItemType()){
            case WEAPON:
                return weaponsEquipped.contains(i);
            case ARMOR:
                return armorWorn.contains(i);
            default:
                return false;
        }
    }

    public void sellItem(Item i, Inventory merchant){
        /*
        Handles all handover logic and gold gaining and equipped or not validation
         */

        // not equipped?
        if (itemIsEquipped(i)){
            ConsoleColors.printInColor(RED_BOLD, String.format("⚠️Hero %s can't sell an item that is equipped", name));
        }
        else{
            tradeItem(i, this.inventory, merchant);
            gold += i.getPrice();
            i.setPrice(i.getPrice() * 2);
        }

    }

//    GETTERS
    public int getGold() {
        return gold;
    }

    public int getMp() {
        return mp;
    }

    public int getXp() {
        return xp;
    }

    public int getMpMax() {
        return mpMax;
    }

    public HeroType getHeroType() {
        return heroType;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getDexterity() {
        return dexterity;
    }

    public int getStrength() {
        return strength;
    }
}
