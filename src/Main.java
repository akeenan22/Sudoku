public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        board.rngBoard(100);
        new Canvas(board);
    }
}