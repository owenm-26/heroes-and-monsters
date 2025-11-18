package Items;

public class Armor extends Item{

    private int damageReduction;

    public Armor(String name, int level, int price, int usesLeft, int damageReduction){
        this.name=name;
        this.level=level;
        this.price = price;
        this.usesLeft=usesLeft;
        this.type=ItemType.ARMOR;
        this.damageReduction = damageReduction;
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
}
