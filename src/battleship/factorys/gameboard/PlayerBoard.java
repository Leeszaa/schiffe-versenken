package battleship.factorys.gameboard;

import java.util.HashMap;
import java.util.Map;

import java.awt.*;

import battleship.factorys.ships.*;

public class PlayerBoard implements IGameBoard {
    private final int BOARD_SIZE = 10;
    private final Map<Point, IShip> ships;

    public PlayerBoard() {
        ships = new HashMap<>();
    }

    @Override
    public void placeShip(int x, int y, IShip ship, boolean isHorizontal) {
        if (isValidPlacement(x, y, ship.getShipSize(), isHorizontal)) {
            for (int i = 0; i < ship.getShipSize(); i++) {
                int newX = isHorizontal ? x + i : x;
                int newY = isHorizontal ? y : y + i;
                ships.put(new Point(newX, newY), ship);
            }
        } else {
            throw new IllegalArgumentException("Ungültige Schiffsplatzierung.");
        }
    }

    @Override
    public void receiveHit(int x, int y, boolean isHit) {
        if (ships.containsKey(new Point(x, y))) {
            IShip ship = ships.get(new Point(x, y));
            // Hier die Logik zum Umgang mit Schiffstreffern einfügen
            
            //if (/* Bedingung für Schiff versenkt */) { 
                ships.remove(new Point(x, y));
            //}
        }
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
        return ships.containsKey(new Point(x, y));
    }

    // Hilfsmethode, um die Gültigkeit der Schiffsplatzierung zu überprüfen
    private boolean isValidPlacement(int x, int y, int shipSize, boolean isHorizontal) {
        if (x < 0 || y < 0 || x >= BOARD_SIZE || y >= BOARD_SIZE) {
            return false; // Außerhalb des Spielfelds
        }

        for (int i = 0; i < shipSize; i++) {
            int newX = isHorizontal ? x + i : x;
            int newY = isHorizontal ? y : y + i;

            if (newX >= BOARD_SIZE || newY >= BOARD_SIZE || ships.containsKey(new Point(newX, newY))) {
                return false; // Überschneidung oder außerhalb des Spielfelds
            }
        }
        return true; // Gültige Platzierung
    }
}
