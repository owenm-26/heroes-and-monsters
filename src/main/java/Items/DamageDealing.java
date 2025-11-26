package Items;

public interface DamageDealing {

    int getDamageDealt(int attribute);

    ItemType getItemType();

    String getName();

    boolean useItem();
}
