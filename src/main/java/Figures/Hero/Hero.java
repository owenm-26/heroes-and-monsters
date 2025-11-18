package Figures.Hero;

import Figures.Figure;
import GameBoard.HMEffect;
import Items.Armor;
import Items.Item;
import Items.ItemType;
import Items.Potion.Potion;
import Items.Potion.PotionType;
import Items.Weapon;
import UI.ConsoleColors;

import java.util.*;

import static Items.Potion.PotionType.POTION_DURATION_LENGTH;
import static Validators.Integers.validatePositiveIntegers;

public class Hero extends Figure {
    private int xp;
    private int mp;
    private int mpMax;
    private int gold;
    private List<Armor> armorWorn;
    private List<Weapon> weaponsEquipped;
    private int numberOfHands;
    private HeroType heroType;

    // attributes
    private int agility;
    private int strength;
    private int dexterity;

    public Hero(String name, int hpMax, int mpMax, int hands, HeroType heroType){
        validatePositiveIntegers(hpMax, mpMax, hands);
        this.name = name;
        this.hpMax = hpMax;
        this.mpMax = mpMax;
        this.heroType = heroType;
        this.numberOfHands = hands;
        weaponsEquipped = new ArrayList<>();
        armorWorn = new ArrayList<>();
        activeEffects = new HashSet<>();
        hp = hpMax;
        mp = mpMax;
        level = 1;

        //TODO: Set agility, strength, dexterity based on HeroType
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
        mp = Math.max(mpMax, mp + p);
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

    public void equipItem(Item i){
        /*
        Public method that handles which equip and unequip logic to use
         */
        if (!i.isEquipable()) throw new IllegalArgumentException("This item is not equipable");

        if (i.getItemType() == ItemType.WEAPON) equipWeapon((Weapon) i);
        else equipArmor((Armor) i);
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
}
