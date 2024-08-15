package battleship;

public class PlayerBoard implements IGameBoard {
    private final int[][] board;
    
    public PlayerBoard() {
        board = new int[10][10];
    }

    @Override
    public void placeShip(int x, int y, Ship ship) {

        System.out.println("Ship placed at: " + x + "," + y);
    }

    @Override
    public boolean receiveHit(int x, int y) {

        return false;
    }

    @Override
    public void display() {
        System.out.println("Player Board");
    }
}
