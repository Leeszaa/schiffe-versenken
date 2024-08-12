package battleship;

public class Ship {
    private int size;
    private int hits;
    private boolean sunk;
    private int[][] position;

    public Ship(int size) {
        this.size = size;
        this.hits = 0;
        this.sunk = false;
    }

    public void place(int x, int y, boolean horizontal) {
        position = new int[size][2];
        for (int i = 0; i < size; i++) {
            if (horizontal) {
                position[i][0] = x + i;
                position[i][1] = y;
            } else {
                position[i][0] = x;
                position[i][1] = y + i;
            }
        }
    }

    public boolean isHit(int x, int y) {
        for (int i = 0; i < size; i++) {
            if (position[i][0] == x && position[i][1] == y) {
                hits++;
                if (hits == size) {
                    sunk = true;
                }
                return true;
            }
        }
        return false;
    }

    public boolean isSunk() {
        return sunk;
    }
    
    public int[][] getPosition() {
        return position;
    }

    public int getSize() {
        return size;
    }
}
