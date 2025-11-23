package Items.Spell;

public enum SpellType {

    ICE("❄️"),
    FIRE("🔥"),
    LIGHTNING("⚡️");

    private String symbol;
    SpellType(String symbol){
        this.symbol = symbol;

    }

    public String getSymbol() {
        return symbol;
    }
}
