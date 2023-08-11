import java.util.Random;

public class Board {
    public int[][] board = new int[9][9];
    public int currX;
    public int currY;
    public boolean noteMode = false;
    public boolean[][][] notes = new boolean[9][9][9];

    public boolean[][] editable = new boolean[9][9];
    public Board previous;

    public boolean hasWon() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (board[i][j] == 0)
                    return false;
        return validateBoard();
    }

    public boolean validateBoard() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (!validateSquare(i, j))
                    return false;
        return true;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean validateSquare(int x, int y) {
        if (board[x][y] == 0) return true;
        for (int i = 0; i < 9; i++)
            if (i != x && board[i][y] == board[x][y])
                return false;
            else if (i != y && board[x][i] == board[x][y])
                return false;
            else if (i != findInBox(x, y) && valInBox(i, findBox(x, y)) == board[x][y])
                return false;
        return true;
    }

    public void autoNote(int x, int y) {
        for (int i = 0; i < 9; i++)
            notes[x][y][i] = true;
        for (int i = 0; i < 9; i++) {
            if (board[i][y] != 0)
                notes[x][y][board[i][y] - 1] = false;
            if (board[x][i] != 0)
                notes[x][y][board[x][i] - 1] = false;
            if (valInBox(i, findBox(x, y)) != 0)
                notes[x][y][valInBox(i, findBox(x, y)) - 1] = false;
        }
    }

    public void set(int x, int y, int val) {
        if (!editable[x][y]) return;
        if (val == 0 || val == board[x][y]) {
            board[x][y] = 0;
            return;
        }
        board[x][y] = val;
        for (int i = 0; i < 9; i++) {
            if (board[i][y] == 0)
                notes[i][y][val - 1] = false;
            if (board[x][i] == 0)
                notes[x][i][val - 1] = false;
            if (valInBox(i, findBox(x, y)) == 0)
                notes[xInBox(i, findBox(x, y))][yInBox(i, findBox(x, y))][val - 1] = false;
        }
    }

    public void autoNoteAll() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (board[i][j] == 0)
                    autoNote(i, j);
    }

    public int findBox(int x, int y) {
        return x / 3 + 3 * (y / 3);
    }

    public int findInBox(int x, int y) {
        return x % 3 + 3 * (y % 3);
    }

    public int xInBox(int n, int boxNum) {
        return n % 3 + 3 * (boxNum % 3);
    }

    public int yInBox(int n, int boxNum) {
        return n / 3 + 3 * (boxNum / 3);
    }

    public int valInBox(int n, int boxNum) {
        return board[xInBox(n, boxNum)][yInBox(n, boxNum)];
    }

    public int currVal() {
        return board[currX][currY];
    }

    @SuppressWarnings("all")
    public void rngBoard(int emptySquares) {
        setEditable(true);
        while (!hasWon()) {
            clearBoard();
            autoNoteAll();
            while (setMinNotes());
        }
        for (int i = 0; i < emptySquares; i++)
            if (!clearValidSquare()) break;
        clearAllNotes();
        setEditable();
    }

    public void setEditable() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                editable[i][j] = board[i][j] == 0;
    }

    public void setEditable(boolean b) {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                editable[i][j] = b;
    }

    public boolean setRandomFromNote(int x, int y) {
        int i, seed = new Random().nextInt(0, 9);
        for (i = 0; i < 9; i++)
            if (notes[x][y][(seed + i) % 9])
                break;
        if (i == 9) return false;
        set(x, y, (seed + i) % 9 + 1);
        return true;
    }

    public boolean setMinNotes() {
        int minX = 0, minY = 0;
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (board[i][j] == 0 &&
                    (board[minX][minY] != 0 || countNotes(i, j) < countNotes(minX, minY))) {
                    minX = i;
                    minY = j;
                }
        if (board[minX][minY] != 0) return false;
        return setRandomFromNote(minX, minY);
    }

    public int minNotesX() {
        int minX = 0, minY = 0;
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (board[i][j] == 0 &&
                    (board[minX][minY] != 0 || countNotes(i, j) < countNotes(minX, minY))) {
                    minX = i;
                    minY = j;
                }
        return minX;
    }

    public int minNotesY() {
        int minX = 0, minY = 0;
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (board[i][j] == 0 &&
                    (board[minX][minY] != 0 || countNotes(i, j) < countNotes(minX, minY))) {
                    minX = i;
                    minY = j;
                }
        return minY;
    }

    public int minNotes() {
        int minX = 0, minY = 0;
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (board[i][j] == 0 &&
                    (board[minX][minY] != 0 || countNotes(i, j) < countNotes(minX, minY))) {
                    minX = i;
                    minY = j;
                }
        if (board[minX][minY] != 0) return 10;
        return countNotes(minX, minY);
    }

    public boolean clearValidSquare() {
        Random r = new Random();
        int x = r.nextInt(0, 9), y = r.nextInt(0, 9), i, j;
        for (i = 0; i < 9; i++)
            for (j = 0; j < 9; j++) {
                Board b = clone();
                b.board[(x + i) % 9][(y + j) % 9] = 0;
                if (b.canSolve()) {
                    board[(x + i) % 9][(y + j) % 9] = 0;
                    return true;
                }
            }
        return false;
    }

    public int countNotes(int x, int y) {
        int sum = 0;
        for (int i = 0; i < 9; i++)
            if (notes[x][y][i])
                sum++;
        return sum;
    }

    public void clearBoard() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                board[i][j] = 0;
                for (int k = 0; k < 9; k++)
                    notes[i][j][k] = false;
            }
    }

    public void clearAllNotes() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                for (int k = 0; k < 9; k++)
                    notes[i][j][k] = false;
    }

    @SuppressWarnings("all")
    public boolean solve() {
        int i = 0;
        while (!hasWon() && i++ < 300) {
            autoNoteAll();
            while (setMinNotes());
        }
        return i <= 300;
    }

    public boolean canSolve() {
        return solutions() == 1;
    }

    public int solutions() {
        Board b = clone();
        b.autoNoteAll();
        while (b.minNotes() == 1) b.setMinNotes();
        if (b.hasWon()) return 1;
        if (b.validateBoard()) {
            int count = 0;
            for (int i = 0; i < 9; i++)
                if (b.notes[b.minNotesX()][b.minNotesY()][i]) {
                    Board c = b.clone();
                    c.set(c.minNotesX(), c.minNotesY(), i + 1);
                    count += c.solutions();
                }
            return count;
        }
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                if (board[x][y] == 0) sb.append("  ");
                else sb.append(" ").append(board[x][y]);
                if (x == 2 || x == 5) sb.append(" |");
            }
            sb.append("\n");
            if (y == 2 || y == 5) sb.append("-------+-------+-------\n");
        }
        return sb.toString();
    }

    @SuppressWarnings("all")
    public Board clone() {
        Board b = new Board();
        b.currX = currX;
        b.currY = currY;
        b.noteMode = noteMode;
        b.previous = previous;
        for (int i = 0; i < 9; i++) {
            System.arraycopy(board[i], 0, b.board[i], 0, 9);
            System.arraycopy(editable[i], 0, b.editable[i], 0, 9);
            for (int j = 0; j < 9; j++)
                System.arraycopy(notes[i][j], 0, b.notes[i][j], 0, 9);
        }
        return b;
    }

    public boolean partialEq(Board b) {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (b.board[i][j] != board[i][j])
                    return false;
                else for (int k = 0; k < 9; k++)
                    if (b.notes[i][j][k] != notes[i][j][k])
                        return false;
        return true;
    }

    public void set(Board b) {
        currX = b.currX;
        currY = b.currY;
        noteMode = b.noteMode;
        previous = b.previous;
        for (int i = 0; i < 9; i++) {
            System.arraycopy(b.board[i], 0, board[i], 0, 9);
            System.arraycopy(b.editable[i], 0, editable[i], 0, 9);
            for (int j = 0; j < 9; j++)
                System.arraycopy(b.notes[i][j], 0, notes[i][j], 0, 9);
        }
    }

    public boolean set(char input) {
        Board b = clone();
        if (input == 'q') return false;
        if (input == 'r') rngBoard(100);
        if (input == 'n') noteMode = !noteMode;
        if (input == 'h') autoNote(currX, currY);
        if (input == 'x') autoNoteAll();
        if (input == 'w') if (currY > 0) currY--;
        if (input == 's') if (currY < 8) currY++;
        if (input == 'a') if (currX > 0) currX--;
        if (input == 'd') if (currX < 8) currX++;
        if (input == 't') {
            currX = minNotesX();
            currY = minNotesY();
        }
        if (input == 'y') System.out.println(canSolve());
        if (Character.isDigit(input))
            if (noteMode)
                if (Integer.parseInt(input + "") == 0)
                    for (int i = 0; i < 9; i++)
                        notes[currX][currY][i] = false;
                else
                    notes[currX][currY][Integer.parseInt(input + "") - 1] ^= true;
            else
                set(currX, currY, Integer.parseInt(input + ""));
        if (!partialEq(b)) previous = b;
        if (previous != null && input == 'z') set(previous);
        return true;
    }
}
