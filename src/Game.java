import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Game extends JFrame {
    private Board board;
    private int boardSize = 20;
    private int mineCount = 80;
    private GridUI gridUI;

    public Game() {
        board = new Board(boardSize, mineCount);
        gridUI = new GridUI(mineCount);
        add(gridUI);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    class GridUI extends JPanel {
        public static final int CELL_SIZE = 30;

        private Image imageCell;
        private Image imageFlag;
        private Image imageMine;
        private int flagsLeft;

        private void updateFlagsLeft() {
            int flags = 0;
            for(int row = 0; row < boardSize; row++) {
                for(int col = 0; col < boardSize; col++) {
                    Cell cell = board.getCell(row, col);
                    if(cell.isFlagged()) {
                        flags++;
                    }
                }
            }
            flagsLeft = mineCount - flags;
        }

        public GridUI(int mineCount) {
            setPreferredSize(new Dimension(boardSize * CELL_SIZE,
                    boardSize * CELL_SIZE));
            imageCell = new ImageIcon("imgs/Cell.png").getImage();
            imageFlag = new ImageIcon("imgs/Flag.png").getImage();
            imageMine = new ImageIcon("imgs/Mine.png").getImage();

            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    int row = e.getY() / CELL_SIZE;
                    int col = e.getX() / CELL_SIZE;
                    System.out.println("Clicked on row: " + row + ", col: " + col);

                    Cell cell = board.getCell(row, col);
                    if(!cell.isCovered()) {
                        return;
                    }

                    if(SwingUtilities.isRightMouseButton(e)) {
                        cell.setFlagged(!cell.isFlagged());
                    } else {
                        if(cell.isFlagged()) {
                            return;
                        }
                        board.revealCellRecursively(row, col);
                        if(board.mineHasBeenRevealed()) {
                            JOptionPane.showMessageDialog(Game.this,
                                    "Game Over",
                                    "You lose",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                    }
                    repaint();
                }
            });
            flagsLeft = mineCount;
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            for(int row = 0; row < boardSize; row++) {
                for(int col = 0; col < boardSize; col++) {
                    paintCell(g, row, col);
                }
            }

            // draw flag counter
            g.setColor(Color.black);
            g.drawString("Flags left: " + flagsLeft, 10, CELL_SIZE * (boardSize + 1));
        }

        private void paintCell(Graphics g, int row, int col) {
            int x = col * CELL_SIZE;
            int y = row * CELL_SIZE;
        
            Cell cell = board.getCell(row, col);
            if (cell.isCovered()) {
                g.drawImage(imageCell, x, y, CELL_SIZE, CELL_SIZE, null, null);
                if (cell.isFlagged()) {
                    g.drawImage(imageFlag, x, y, CELL_SIZE, CELL_SIZE, null, null);
                }
            } else {
                g.setColor(Color.gray);
                g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                g.setColor(Color.lightGray);
                g.fillRect(x + 1, y + 1, CELL_SIZE - 2, CELL_SIZE - 2);
        
                if (cell.hasMine()) {
                    g.drawImage(imageMine, x, y, CELL_SIZE, CELL_SIZE, null, null);
                } else if (cell.getAdjacentMines() > 0) {
                    int num = cell.getAdjacentMines();
                    Color color;
                    switch (num) {
                        case 1:
                            color = Color.blue;
                            break;
                        case 2:
                            color = Color.green;
                            break;
                        case 3:
                            color = Color.red;
                            break;
                        case 4:
                            color = Color.magenta;
                            break;
                        case 5:
                            color = Color.orange;
                            break;
                        case 6:
                            color = Color.cyan;
                            break;
                        case 7:
                        case 8:
                            color = Color.darkGray;
                            break;
                        default:
                            color = Color.black;
                            break;
                    }
                    g.setColor(color);
                    g.drawString(num + "", x + 10, y + 18);
                }
            }
        }
    }

    public static void main(String[] args) {
        new Game();
    }
}
