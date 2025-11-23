package Battle;

import Items.Potion.PotionType;

public enum BattleCommand {

    CHANGE_WEAPON("Change Weapons"),
    CHANGE_ARMOR("Change Armor"),
    TAKE_POTION("Take Potion"),
    ATTACK("Attack");

    private String name;

    BattleCommand(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static String[] getBackpackActions(){
        return new String[]{CHANGE_WEAPON.name, CHANGE_ARMOR.name, TAKE_POTION.name};
    }

    public static String[] getAllActions(){
        String[] actions = new String[BattleCommand.values().length];
        int i =0;
        for (BattleCommand c: BattleCommand.values()){
            actions[i++] = c.name;
        }

        return actions;
    }

    public static BattleCommand fromName(String n) {
        for (BattleCommand a : values()) {
            if (a.name.equalsIgnoreCase(n))
                return a;
        }
        throw new IllegalArgumentException("Invalid BattleCommand: " + n);
    }
}
