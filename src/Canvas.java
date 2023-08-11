import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Contains the main function and the logic for graphics
 * */
public class Canvas extends JPanel implements KeyListener, MouseListener {
    private final Board b;
    public static final int boardDim = 9;
    private final int height = 800;
    private final int width = 800;
    private final int boxH = (int)(height / (1.1 * boardDim));
    private final int boxW = (int)(width / (1.1 * boardDim));

    /**
     * initializes the canvas, and the values for the colors corresponding to values
     * */
    public Canvas (Board b) {
        JFrame frame = new JFrame("Sudoku");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(width - 65, height - 40));
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
        this.b = b;
        frame.addKeyListener(this);
        frame.addMouseListener(this);
        frame.setFocusable(true);
    }

    /**
     * draws the board
     * */
    public void paint(Graphics g) {
        super.paint(g);
        int fontSize = 45;
        for (int i = 0; i < boardDim; i++) {
            for (int j = 0; j < boardDim; j++) {
                g.setColor(Color.WHITE);
                if (i == b.currX || j == b.currY || b.findBox(i, j) == b.findBox(b.currX, b.currY))
                    if (b.noteMode)
                        g.setColor(new Color(225, 255, 240));
                    else
                        g.setColor(new Color(225, 240, 255));
                if (i == b.currX && j == b.currY)
                    if (b.noteMode)
                        g.setColor(new Color(195, 255, 225));
                    else
                        g.setColor(new Color(195, 225, 255));
                if (!b.validateSquare(i, j))
                    g.setColor(new Color(255, 195, 225));

                g.fillRect(boxW * i, boxH * j, boxW, boxH);
                g.setColor(Color.BLACK);
                if (b.currVal() == b.board[i][j])
                    g.setFont(new Font("Helvetica", Font.BOLD, fontSize * 11 / 10));
                else
                    g.setFont(new Font("Helvetica", Font.PLAIN, fontSize));

                if (b.board[i][j] != 0) {
                    if (b.editable[i][j]) g.setColor(Color.BLUE);
                    g.drawString(String.valueOf(b.board[i][j]),
                            boxW * i + boxW / 3,
                            boxH * j + 2 * boxH / 3);
                    g.setColor(Color.BLACK);
                } else
                    for (int k = 0; k < 9; k++) {
                        if (b.currVal() == k + 1)
                            g.setFont(new Font("Helvetica", Font.BOLD, fontSize / 3 * 12 / 10));
                        else
                            g.setFont(new Font("Helvetica", Font.PLAIN, fontSize / 3));
                        if (b.notes[i][j][k])
                            g.drawString(Integer.toString(k + 1, 10),
                                    boxW * i + boxW * (k % 3) / 3 + boxW / 9,
                                    boxH * j + boxH * (k / 3) / 3 + 2 * boxW / 9);
                    }
                g.drawRect(boxW * i, boxH * j, boxW, boxH);
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int lineW = 4;
                if (j != 0)
                    g.fillRect(3 * boxW * i, 3 * boxH * j - lineW / 2, 3 * boxW, lineW);
                if (i != 0)
                    g.fillRect(3 * boxW * i - lineW / 2, 3 * boxH * j, lineW, 3 * boxH);
            }
        }
        if (b.hasWon()) {
            g.setColor(new Color(255, 255, 255, 230));
            g.fillRect(0, boxH * 3, width, boxH * 3);
            g.setColor(new Color(75, 200, 75));
            g.setFont(new Font("Helvetica", Font.BOLD, fontSize * 3));
            g.drawString("You Won!", width / 18, height / 2);
        }

    }

    public void keyTyped(KeyEvent e) {

    }

    /**
     * handles the input and output for the game
     * */
    public void keyPressed(KeyEvent e) {
        if ("1234567890qwertyuiopasdfghjklzxcvbnmz".indexOf(customKeyChar(e)) == -1) return;
        boolean move = b.set(customKeyChar(e));
        if (!move) System.exit(0);
        repaint();
    }

    public static char customKeyChar(KeyEvent e) {
        return switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> 'w';
            case KeyEvent.VK_LEFT -> 'a';
            case KeyEvent.VK_DOWN -> 's';
            case KeyEvent.VK_RIGHT -> 'd';
            case KeyEvent.VK_BACK_SPACE -> '0';
            default -> e.getKeyChar();
        };
    }

    public void keyReleased(KeyEvent e) {

    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {
        if (e.getX() < 9 * boxW && e.getY() < 9 * boxH + 30) {
            b.currX = e.getX() / boxW;
            b.currY = (e.getY() - 30) / boxH;
        }
        repaint();
    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }
}
