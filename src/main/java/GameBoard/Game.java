package GameBoard;

public class Game {
    private Board board;

    public Game(Board b){
        this.board = b;
    }
    public Game(int n){
        board = new Board(n);
    }

    public Game(){
        this(6);
    }
}
