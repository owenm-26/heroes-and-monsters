package Items;

import Data.LoadableFromText;
import Data.TextDataLoader;
import Figures.Hero.HeroType;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static Data.TextDataLoader.getAllSourceFileNames;

public class Armor extends Item implements LoadableFromText {

    private int damageReduction;
    private static final int DEFAULT_USES_LEFT=10;

    public Armor(String name, int level, int price, int usesLeft, int damageReduction){
        this.name=name;
        this.level=level;
        this.price = price;
        this.usesLeft=usesLeft;
        this.type=ItemType.ARMOR;
        this.damageReduction = damageReduction;
    }

    public Armor(){
        usesLeft = DEFAULT_USES_LEFT;
        type=ItemType.ARMOR;
    }

    @Override
    protected String printOutOfUsesMessage() {
        return String.format("Your Armor piece %s broke from wear!", name);
    }

    public int getDamageReduction() {
        return damageReduction;
    }

    public boolean isEquipable(){
        return true;
    }

    @Override
    public String getItemDescriptionOneLiner() {
        return String.format(
                "🛡️ %-20s lvl:%-2d  reduct:%-4d  uses:%-3d  %4d gold",
                name, level, damageReduction, usesLeft, price
        );
    }


    public void loadFromMap(Map<String, String> map){
//        Name/cost/required level/damage reduction
        this.name = map.get("Name");
        this.price= Integer.parseInt(map.get("cost"));
        this.level = Integer.parseInt(map.get("required level"));
        this.damageReduction = Integer.parseInt(map.get("damage reduction"));
    }

    public static List<Armor> getAllArmorOptions(){
        ArrayList<Armor> armor = new ArrayList<>();
        try{
            for(String filename: getAllSourceFileNames("Armor")){
                armor.addAll(TextDataLoader.load("Armor", filename, Armor.class));
            }
        }catch (Exception e){
            System.out.format("Failed to load all Armor: %s\n", e);
        }
        return armor;
    }
}
