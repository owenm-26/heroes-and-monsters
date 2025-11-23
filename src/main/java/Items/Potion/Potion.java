package Items.Potion;

import Data.LoadableFromText;
import Data.TextDataLoader;
import Items.Item;
import Items.ItemType;
import Items.Spell.Spell;
import Items.Spell.SpellType;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Data.TextDataLoader.getAllSourceFileNames;
import static Items.Potion.PotionType.fromName;

public class Potion extends Item implements LoadableFromText {

    private HashMap<PotionType, Integer> effects;
    private static final int DEFAULT_USES_LEFT=1;

    public Potion(String name, int level, int price, HashMap<PotionType, Integer> effects){
        this.name=name;
        this.level=level;
        this.price = price;
        this.usesLeft=DEFAULT_USES_LEFT;
        this.type= ItemType.POTION;
        this.effects = effects;
    }

    public Potion(){
        usesLeft = DEFAULT_USES_LEFT;
        type=ItemType.POTION;
        effects = new HashMap<>();
    }

    @Override
    protected String printOutOfUsesMessage() {
        return String.format("You have used your %s potion. Removing from inventory");
    }

    public HashMap<PotionType, Integer> getEffects() {
        return effects;
    }

    public boolean isEquipable(){
        return false;
    }

    @Override
    public String getItemDescriptionOneLiner() {
        StringBuilder effectString = new StringBuilder();
        for (PotionType p : effects.keySet()) {
            effectString.append(String.format("%s:+%d  ", p.getName(), effects.get(p)));
        }

        return String.format(
                "🧪 %-20s lvl:%-2d  uses:%-3d  %-40s %4d gold",
                name, level, usesLeft, effectString.toString(), price
        );
    }


    private void setAttributesAffected(String attributesAffected, int increase){
        if (attributesAffected.length() == 0) throw new IllegalArgumentException("attributesAffected is length 0 in setAttributesAffected()");
        String[] attributes = attributesAffected.split("\\s*/\\s*");
        for (String attr: attributes){
            PotionType p = fromName(attr);
            effects.put(p, increase);
        }
        System.out.println(attributesAffected +" - " + increase);
        System.out.println(this.getItemDescriptionOneLiner());

    }

    public void loadFromMap(Map<String, String> map){
        //    Name/cost/required level/attribute increase/attribute affected
        this.name = map.get("Name");
        this.price= Integer.parseInt(map.get("cost"));
        this.level = Integer.parseInt(map.get("required level"));
        String attributesAffected = map.get("attribute affected");
        int increase = Integer.parseInt(map.get("attribute increase"));
        setAttributesAffected(attributesAffected, increase);

    }

    public static List<Potion> getAllPotionOptions(){
        ArrayList<Potion> potions = new ArrayList<>();
        try{
            for(String filename: getAllSourceFileNames("Potions")){
                potions.addAll(TextDataLoader.load("Potions", filename, Potion.class));
            }
        }catch (Exception e){
            System.out.format("Failed to load all potions: %s\n", e);
            e.printStackTrace();
        }
        return potions;
    }
}
