package GameBoard;

public class Game {
    private Board board;
    public Game(int n){
        board = new Board(n);
        board.displayBoard();
    }

    public Game(){
        this(6);
    }
}
