package Items.Spell;

import Items.Item;
import Items.ItemType;

public class Spell extends Item {
    private int damage;
    private int mpCost;
    private SpellType spellType;

    public Spell(String name, int level, int price, int damage, int mpCost, SpellType spellType){
        this.name=name;
        this.level=level;
        this.price = price;
        this.usesLeft=5;
        this.type = ItemType.SPELL;
        this.damage = damage;
        this.spellType = spellType;
        this.mpCost = mpCost;
    }

    @Override
    protected String printOutOfUsesMessage() {
        return String.format("You have used up your last charge of your %s spell. Removing from inventory", name);
    }

    public int getDamage() {
        return damage;
    }

    public int getMpCost() {
        return mpCost;
    }

    public SpellType getSpellType() {
        return spellType;
    }

    public boolean isEquipable(){
        return false;
    }

    @Override
    public String getItemDescriptionOneLiner() {
        return String.format("🪄️ %s - lvl%d   %d damage points  %s type   %d mana   %d uses left   %d gold", name, level, damage, spellType.getSymbol(), mpCost, usesLeft, price);
    }
}
