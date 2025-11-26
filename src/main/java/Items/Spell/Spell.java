package Items.Spell;

import Data.LoadableFromText;
import Data.TextDataLoader;
import Figures.Hero.HeroType;
import Items.DamageDealing;
import Items.Item;
import Items.ItemType;
import Items.Weapon;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static Data.TextDataLoader.getAllSourceFileNames;

public class Spell extends Item implements LoadableFromText, DamageDealing {
    private int damage;
    private int mpCost;
    private SpellType spellType;
    private static final int DEFAULT_USES_LEFT=5;

    public Spell(String name, int level, int price, int damage, int mpCost, SpellType spellType){
        this.name=name;
        this.level=level;
        this.price = price;
        this.usesLeft=DEFAULT_USES_LEFT;
        this.type = ItemType.SPELL;
        this.damage = damage;
        this.spellType = spellType;
        this.mpCost = mpCost;
    }

    public Spell(){
        usesLeft = DEFAULT_USES_LEFT;
        type=ItemType.SPELL;
    }

    @Override
    protected String printOutOfUsesMessage() {
        return String.format("You have used up your last charge of your %s spell. Removing from inventory", name);
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
        return String.format(
                "🪄 %-20s lvl:%-2d  dmg:%-4d  type:%-2s  mana:%-4d  uses:%-3d  %4d gold",
                name, level, damage, spellType.getSymbol(), mpCost, usesLeft, price
        );
    }

    public int getDamageDealt(int dexterity){
        return  (int) (( (float) dexterity / 5000) * damage);
    }

    private void setSpellTypeFromFileName(String fileName){
        if (fileName.length() == 0) throw new IllegalArgumentException("Filename is length 0 in setHeroTypeFromFileName()");
        switch (fileName){
            case "firespells":
                spellType = SpellType.FIRE;
                break;
            case "icespells":
                spellType = SpellType.ICE;
                break;
            case "lightningspells":
                spellType = SpellType.LIGHTNING;
                break;
            default:
                throw new ValueException("Something went wrong in setSpellTypeFromFileName()");
        }
    }
    public void loadFromMap(Map<String, String> map){
    //    Name/cost/required level/damage/mana cost
        this.name = map.get("Name");
        this.price= Integer.parseInt(map.get("cost"));
        this.level = Integer.parseInt(map.get("required level"));
        this.damage = Integer.parseInt(map.get("damage"));
        this.mpCost = Integer.parseInt(map.get("mana cost"));

        // Spell Type
        setSpellTypeFromFileName(map.get("file name"));
    }

    public static List<Spell> getAllSpellOptions(){
        ArrayList<Spell> spells = new ArrayList<>();
        try{
            for(String filename: getAllSourceFileNames("Spells")){
                spells.addAll(TextDataLoader.load("Spells", filename, Spell.class));
            }
        }catch (Exception e){
            System.out.format("Failed to load all spells: %s\n", e);
        }
        return spells;
    }
}
