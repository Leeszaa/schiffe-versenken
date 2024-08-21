package battleship.factorys.gameboard;

import java.util.HashMap;
import java.util.Map;
import java.awt.*;
import battleship.factorys.ships.*;

public class TargetingBoard implements IGameBoard {
    private final int BOARD_SIZE = 10;
    private final Map<Point, IShip> ships;
    private final Map<Point, Boolean> hitAttempts;

    public TargetingBoard() {
        ships = new HashMap<>();
        hitAttempts = new HashMap<>();
    }

    @Override
    public void placeShip(int x, int y, IShip ship, boolean isHorizontal) {
        // Implementation for placing a ship
    }

    @Override
    public void receiveHit(int x, int y, boolean isHit) {
        Point point = new Point(x, y);
        hitAttempts.put(point, isHit);
    }

    @Override
    public void display() {
        System.out.println("Player Board");
    }

    @Override
    public Map<Point, IShip> getShipLocations() {
        return new HashMap<>(ships);
    }

    @Override
    public boolean isShipHit(int x, int y) {
        return false;
    }

    public Map<Point, Boolean> getHitAttempts() {
        return new HashMap<>(hitAttempts);
    }

    private boolean isValidPlacement(int x, int y, int shipSize, boolean isHorizontal) {
        return true;
    }
}