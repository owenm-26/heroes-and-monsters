package Items.Potion;

import Items.Item;
import Items.ItemType;

import java.util.HashMap;
import java.util.List;

public class Potion extends Item {

    private HashMap<PotionType, Integer> effects;

    public Potion(String name, int level, int price, HashMap<PotionType, Integer> effects){
        this.name=name;
        this.level=level;
        this.price = price;
        this.usesLeft=1;
        this.type= ItemType.POTION;
        this.effects = effects;

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
        StringBuilder b = new StringBuilder();
        for (PotionType name: effects.keySet()){
            b.append(String.format("(%s - %d) ", name.getName(), effects.get(name)));
        }
        String effects = b.toString();
        return String.format("🧪️ %s - lvl%d   %d uses left  EFFECTS: %s %d gold", name, level, usesLeft, effects, price);
    }
}
