package battleship;

public class Player {
    private GameBoard ownBoard;
    private GameBoard opponentBoard;

    public Player() {
        ownBoard = new GameBoard();
        opponentBoard = new GameBoard();
    }

    public GameBoard getOwnBoard() {
        return ownBoard;
    }

    public GameBoard getOpponentBoard() {
        return opponentBoard;
    }

    public boolean placeShip(Ship ship, int x, int y, boolean horizontal) {
        return ownBoard.placeShip(ship, x, y, horizontal);
    }

    public String attack(Player opponent, int x, int y) {
        return opponent.getOwnBoard().attack(x, y);
    }
}
