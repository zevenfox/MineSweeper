import java.util.Random;
import javax.swing.JOptionPane;

public class Board {
    private int size;
    private int mineCount;
    private Cell [][] cells;

    private static final int [][] ADJACENTS = {
            {-1, -1}, {-1, 0}, {-1, 1},
            { 0, -1}, /*   */  { 0, 1},
            { 1, -1}, { 1, 0}, { 1, 1}
    };

    public boolean isGameWon() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Cell cell = cells[row][col];
                if (!cell.hasMine() && cell.isCovered()) {
                    return false; // game is not won yet
                }
            }
        }
    
        // if we get here, all non-mine cells have been exposed
        JOptionPane.showMessageDialog(null, "Congratulations, you won!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }

    public Board(int size, int mineCount) {
        this.size = size;
        this.mineCount = mineCount;
        initCells();
        seedMines();
        generateCellNumbers();
    }

    public Cell getCell(int row, int col) {
        if(row < 0 || row >= size || col < 0 || col >= size) {
            return null;
        }
        return cells[row][col];
    }

    public void revealCellRecursively(int startRow, int startCol) {
        Cell cell = getCell(startRow, startCol);
        if(cell == null || !cell.isCovered()){
            return;
        }

        cell.reveal();
        if(cell.getAdjacentMines() == 0 && !cell.hasMine()) {
            for(int [] p : ADJACENTS) {
                revealCellRecursively(startRow + p[0], startCol + p[1] );
            }
        }
    }

    public boolean mineHasBeenRevealed() {
        for(int row = 0; row < size; row++) {
            for(int col = 0; col < size; col++) {
                Cell cell = getCell(row, col);
                if(cell.hasMine() && !cell.isCovered()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void initCells() {
        cells = new Cell[size][size];
        for(int row = 0; row < size; row++) {
            for(int col = 0; col < size; col++) {
                cells[row][col] = new Cell();
            }
        }
    }

    private void seedMines() {
        int seeded = 0;
        Random random = new Random();
        while(seeded < mineCount) {
            int row = random.nextInt(size);
            int col = random.nextInt(size);
            Cell cell = getCell(row, col);
            if(cell.hasMine()) {
                continue;
            }
            cell.addMine();
            seeded++;
        }
    }

    private void generateCellNumbers() {
        for(int row = 0; row < size; row++) {
            for(int col = 0; col < size; col++) {
                Cell cell = cells[row][col];
                if(cell.hasMine()) {
                    continue;
                }

                int total = 0;
                for(int [] p : ADJACENTS) {
                    Cell adj = getCell(row + p[0], col + p[1]);
                    if(adj != null && adj.hasMine()) {
                        total++;
                    }
                }
                cell.setAdjacentMines(total);
            }
        }
    }
}
