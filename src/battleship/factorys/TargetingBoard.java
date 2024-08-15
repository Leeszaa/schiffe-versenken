package battleship.factorys;

public class TargetingBoard implements IGameBoard {
    private final int[][] board;
    
    public TargetingBoard() {
        board = new int[10][10];
    }

    @Override
    public void placeShip(int x, int y, IShip ship) {
        throw new UnsupportedOperationException("Can't place ships on the targeting board.");
    }

    @Override
    public boolean receiveHit(int x, int y) {
        System.out.println("Marking hit attempt at: " + x + "," + y);
        return true;
    }

    @Override
    public void display() {
        System.out.println("Targeting Board");
    }
}
