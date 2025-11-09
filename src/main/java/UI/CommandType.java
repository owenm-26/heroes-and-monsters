package UI;

public enum CommandType {
    QUIT("q"),
    INFO("i"),
    MARKET("m"),

    UP("w"),
    DOWN("s"),
    LEFT("a"),
    RIGHT("d"),
    YES("y"),
    NO("n");

    private String code;
    CommandType(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
