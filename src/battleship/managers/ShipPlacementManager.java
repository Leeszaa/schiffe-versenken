package battleship.managers;

import battleship.factorys.gameboard.IGameBoard;
import battleship.factorys.player.IPlayer;
import battleship.factorys.ships.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ShipPlacementManager {
    private IPlayer currentPlayer;
    private IPlayer player1;
    private IPlayer player2;
    private Map<String, Integer> currentPlayerShipCounts;
    private Map<String, Integer> shipLimits;
    private ZerstörerFactory zerstörerFactory;
    private SchlachtschiffFactory schlachtschiffFactory;
    private KreuzerFactory kreuzerFactory;
    private U_BootFactory u_BootFactory;

    private static final int TOTAL_SHIPS = 10;

    public ShipPlacementManager(IPlayer player1, IPlayer player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;
        this.currentPlayerShipCounts = new HashMap<>();
        this.shipLimits = new HashMap<>();

        // Initialize factory objects before calling initializeShipLimits
        this.zerstörerFactory = new ZerstörerFactory();
        this.schlachtschiffFactory = new SchlachtschiffFactory();
        this.kreuzerFactory = new KreuzerFactory();
        this.u_BootFactory = new U_BootFactory();

        initializeShipLimits();
    }

    private void initializeShipLimits() {
        shipLimits.put("Schlachtschiff", schlachtschiffFactory.getShipLimit());
        shipLimits.put("Kreuzer", kreuzerFactory.getShipLimit());
        shipLimits.put("Zerstörer", zerstörerFactory.getShipLimit());
        shipLimits.put("U-Boot", u_BootFactory.getShipLimit());
    }

    public void placeShip(int row, int col, int size, boolean isHorizontal, String shipType, JPanel[][] gridCells) {
        for (int i = 0; i < size; i++) {
            int r = isHorizontal ? row : row + i;
            int c = isHorizontal ? col + i : col;

            if (r >= 10 || c >= 10 || isAdjacentToShip(r, c)) {
                throw new IllegalArgumentException("Platzierung ungültig.");
            }
        }

        switch (shipType) {
            case "Schlachtschiff" ->
                currentPlayer.getGameBoard().placeShip(col, row, schlachtschiffFactory.createShip(), isHorizontal);
            case "Kreuzer" ->
                currentPlayer.getGameBoard().placeShip(col, row, kreuzerFactory.createShip(), isHorizontal);
            case "Zerstörer" ->
                currentPlayer.getGameBoard().placeShip(col, row, zerstörerFactory.createShip(), isHorizontal);
            case "U-Boot" ->
                currentPlayer.getGameBoard().placeShip(col, row, u_BootFactory.createShip(), isHorizontal);
        }

        for (int i = 0; i < size; i++) {
            int r = isHorizontal ? row : row + i;
            int c = isHorizontal ? col + i : col;
            gridCells[r][c].setBackground(Color.GRAY);
        }

        currentPlayerShipCounts.put(shipType, currentPlayerShipCounts.getOrDefault(shipType, 0) + 1);

        if (getPlacedShipsCount() >= TOTAL_SHIPS) {
            switchPlayer();
        }
    }

    private void switchPlayer() {
        if (currentPlayer == player1) {
            currentPlayer = player2;
            currentPlayerShipCounts.clear();
        } else {
            currentPlayer = player1;
            currentPlayerShipCounts.clear();
        }
    }

    private boolean isAdjacentToShip(int row, int col) {
        Map<Point, IShip> ships = currentPlayer.getGameBoard().getShipLocations();
        return isAdjacent(row, col, ships);
    }

    private boolean isAdjacent(int row, int col, Map<Point, IShip> ships) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                int r = row + i;
                int c = col + j;
                if (r >= 0 && r < 10 && c >= 0 && c < 10 && ships.containsKey(new Point(c, r))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canPlaceShip(String shipType) {
        int currentCount = currentPlayerShipCounts.getOrDefault(shipType, 0);
        int maxCount = shipLimits.get(shipType);
        return currentCount < maxCount;
    }

    public int getShipSize(String shipType) {
        return switch (shipType) {
            case "Schlachtschiff" -> schlachtschiffFactory.getShipSize();
            case "Kreuzer" -> kreuzerFactory.getShipSize();
            case "Zerstörer" -> zerstörerFactory.getShipSize();
            case "U-Boot" -> u_BootFactory.getShipSize();
            default -> 0;
        };
    }

    public int getPlacedShipsCount() {
        int totalCount = 0;
        for (int count : currentPlayerShipCounts.values()) {
            totalCount += count;
        }
        return totalCount;
    }

    public Map<Point, IShip> getPlayer1ShipLocations() {
        return player1.getGameBoard().getShipLocations();
    }

    public Map<Point, IShip> getPlayer2ShipLocations() {
        return player2.getGameBoard().getShipLocations();
    }

    public String getCurrentPlayer() {
        return currentPlayer == player1 ? "player1" : "player2";
    }
}