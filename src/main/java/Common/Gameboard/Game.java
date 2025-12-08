package Common.Gameboard;

public abstract class Game<T extends Board> {
    private T board;
    protected abstract void initializeGame();

    public abstract void runGame();

    public abstract void printRules();
}
