package Items;

import Data.LoadableFromText;
import Data.TextDataLoader;
import Figures.Hero.Hero;
import Figures.Hero.HeroType;
import UI.ConsoleColors;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static Data.TextDataLoader.getAllSourceFileNames;

public class Weapon extends Item implements LoadableFromText, DamageDealing {
    private int damage;
    private int handsRequired;
    private static final int DEFAULT_USES_LEFT=10;

    public Weapon(String name, int level, int price, int usesLeft, int damage, int handsRequired){
        this.name=name;
        this.level=level;
        this.price = price;
        this.usesLeft=usesLeft;
        this.type=ItemType.WEAPON;
        this.damage = damage;
        this.handsRequired = handsRequired;
    }

    public Weapon(){
        usesLeft = DEFAULT_USES_LEFT;
        type=ItemType.WEAPON;
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
    protected String printOutOfUsesMessage() {
        return String.format("Your weapon %s broke from wear!", name);
    }

    @Override
    public String getItemDescriptionOneLiner() {
        return String.format(
                "🗡️ %-20s lvl:%-2d  dmg:%-4d  hands:%-2d  uses:%-3d  %4d gold",
                name, level, damage, handsRequired, usesLeft, price
        );
    }


    public boolean isEquipable(){
        return true;
    }

    public void loadFromMap(Map<String, String> map){
//        Name/cost/level/damage/required hands
        this.name = map.get("Name");
        this.price= Integer.parseInt(map.get("cost"));
        this.level = Integer.parseInt(map.get("level"));
        this.damage = Integer.parseInt(map.get("damage"));
        this.handsRequired = Integer.parseInt(map.get("required hands"));

    }

    public static List<Weapon> getAllWeaponOptions(){
        ArrayList<Weapon> weapons = new ArrayList<>();
        try{
            for(String filename: getAllSourceFileNames("Weapons")){
                weapons.addAll(TextDataLoader.load("Weapons", filename, Weapon.class));
            }
        }catch (Exception e){
            System.out.format("Failed to load all weapons: %s\n", e);
        }
        return weapons;
    }

    @Override
    public int getDamageDealt(int strength) {
        return (int)((strength + damage) * 0.05);
    }
}
