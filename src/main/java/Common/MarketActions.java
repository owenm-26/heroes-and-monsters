package Common;

public enum MarketActions {

    BUY("BUY"),
    SELL("SELL");

    private String name;
    MarketActions(String name){
        this.name = name;
    }

    public static MarketActions fromName(String n) {
        for (MarketActions a : values()) {
            if (a.name.equalsIgnoreCase(n))
                return a;
        }
        throw new IllegalArgumentException("Invalid Market Action: " + n);
    }

    public String getName(){
        return name;
    }

    public static String[] marketActionsToList(){
        return new String[]{"BUY", "SELL", "EXIT"};
    }
}
