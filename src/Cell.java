public class Cell {
    private boolean mine;
    private boolean flagged;
    private boolean covered;
    private int adjacentMines;

    public Cell() {
        mine = false;
        flagged = false;
        covered = true;
    }

    public boolean hasMine() {
        return mine;
    }

    public void addMine() {
        this.mine = true;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public boolean isCovered() {
        return covered;
    }

    public void reveal() {
        this.covered = false;
    }

    public int getAdjacentMines() {
        return adjacentMines;
    }

    public void setAdjacentMines(int adjacentMines) {
        this.adjacentMines = adjacentMines;
    }
}
