package battleship;

import java.util.ArrayList;
import java.util.List;

public class GameBoard {
    private char[][] board;
    private List<Ship> ships;

    public GameBoard() {
        board = new char[10][10];
        ships = new ArrayList<>();
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = '~'; // Wasser
            }
        }
    }

    public boolean placeShip(Ship ship, int x, int y, boolean horizontal) {
        if (canPlaceShip(ship, x, y, horizontal)) {
            ship.place(x, y, horizontal);
            ships.add(ship);
            for (int[] pos : ship.getPosition()) {
                board[pos[0]][pos[1]] = 'S';
            }
            return true;
        }
        return false;
    }

    private boolean canPlaceShip(Ship ship, int x, int y, boolean horizontal) {
        int length = ship.getSize();
        for (int i = 0; i < length; i++) {
            int newX = horizontal ? x + i : x;
            int newY = horizontal ? y : y + i;

            if (newX >= 10 || newY >= 10 || board[newX][newY] == 'S') {
                return false;
            }
            // Überprüfe angrenzende Felder
            if (!isAdjacentClear(newX, newY)) {
                return false;
            }
        }
        return true;
    }

    private boolean isAdjacentClear(int x, int y) {
        int[] dx = {-1, 0, 1};
        int[] dy = {-1, 0, 1};

        for (int i : dx) {
            for (int j : dy) {
                int newX = x + i;
                int newY = y + j;
                if (newX >= 0 && newX < 10 && newY >= 0 && newY < 10) {
                    if (board[newX][newY] == 'S') {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public String attack(int x, int y) {
        if (board[x][y] == 'S') {
            for (Ship ship : ships) {
                if (ship.isHit(x, y)) {
                    board[x][y] = 'X';
                    return ship.isSunk() ? "Treffer, versenkt" : "Treffer";
                }
            }
        } else {
            board[x][y] = 'O';
        }
        return "Wasser";
    }

    public boolean allShipsSunk() {
        for (Ship ship : ships) {
            if (!ship.isSunk()) {
                return false;
            }
        }
        return true;
    }

    public char[][] getBoard() {
        return board;
    }
}
