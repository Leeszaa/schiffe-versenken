/**
 * @file ShipPlacementManager.java
 *   Manages the placement of ships in the Battleship game.
 */

package battleship.managers;

import battleship.BattleshipGUI;
import battleship.factorys.player.IPlayer;
import battleship.factorys.ships.*;
import battleship.views.SinglePlacementView;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @class ShipPlacementManager
 *        Manages the placement of ships in the Battleship game.
 */
public class SinglePlacementManager {
    private IPlayer currentPlayer;
    /** < The current player placing ships */
    /** < The second player */
    private Map<String, Integer> currentPlayerShipCounts;
    /** < The count of ships placed by the current player */
    private final Map<String, Integer> shipLimits;
    /** < The limits for each type of ship */
    private final ZerstörerFactory zerstörerFactory;
    /** < Factory for creating Zerstörer ships */
    private final SchlachtschiffFactory schlachtschiffFactory;
    /** < Factory for creating Schlachtschiff ships */
    private final KreuzerFactory kreuzerFactory;
    /** < Factory for creating Kreuzer ships */
    private final U_BootFactory u_BootFactory;
    /** < Factory for creating U-Boot ships */
    private final SinglePlacementView placementView;
    /** < The view for ship placement */
    private final BattleshipGUI battleshipGUI;
    /** < Reference to the main GUI */

    private static final int TOTAL_SHIPS = 10;

    /** < Total number of ships to be placed */

    /**
     * Constructor for ShipPlacementManager.
     * 
     * @param player1       The first player.
     * @param player2       The second player.
     * @param placementView The view for ship placement.
     * @param battleshipGUI The main GUI.
     */
    public SinglePlacementManager(IPlayer player1, SinglePlacementView placementView,
            BattleshipGUI battleshipGUI) {
        this.currentPlayer = player1;
        this.currentPlayerShipCounts = new HashMap<>();
        this.shipLimits = new HashMap<>();
        this.placementView = placementView;
        this.battleshipGUI = battleshipGUI;

        // Initialize factory objects before calling initializeShipLimits
        this.zerstörerFactory = new ZerstörerFactory();
        this.schlachtschiffFactory = new SchlachtschiffFactory();
        this.kreuzerFactory = new KreuzerFactory();
        this.u_BootFactory = new U_BootFactory();

        initializeShipLimits();
    }

    /**
     * Initializes the ship limits for each type of ship.
     */
    private void initializeShipLimits() {
        shipLimits.put("Schlachtschiff", schlachtschiffFactory.getShipLimit());
        shipLimits.put("Kreuzer", kreuzerFactory.getShipLimit());
        shipLimits.put("Zerstörer", zerstörerFactory.getShipLimit());
        shipLimits.put("U-Boot", u_BootFactory.getShipLimit());
    }

    /**
     * Places a ship on the game board.
     * 
     * @param row          The row to place the ship.
     * @param col          The column to place the ship.
     * @param size         The size of the ship.
     * @param isHorizontal True if the ship is placed horizontally, false if
     *                     vertically.
     * @param shipType     The type of the ship.
     * @param gridCells    The grid cells of the game board.
     * @throws IllegalArgumentException if the placement is invalid.
     */
    public void placeShip(int row, int col, int size, boolean isHorizontal, String shipType, JPanel[][] gridCells) {
        for (int i = 0; i < size; i++) {
            int r = isHorizontal ? row : row + i;
            int c = isHorizontal ? col + i : col;

            if (r >= 10 || c >= 10 || isAdjacentToShip(r, c)) {
                throw new IllegalArgumentException("Platzierung ungültig.");
            }
        }

        switch (shipType) {
            case "Schlachtschiff":
                currentPlayer.getGameBoard().placeShip(col, row, schlachtschiffFactory.createShip(), isHorizontal);
                break;
            case "Kreuzer":
                currentPlayer.getGameBoard().placeShip(col, row, kreuzerFactory.createShip(), isHorizontal);
                break;
            case "Zerstörer":
                currentPlayer.getGameBoard().placeShip(col, row, zerstörerFactory.createShip(), isHorizontal);
                break;
            case "U-Boot":
                currentPlayer.getGameBoard().placeShip(col, row, u_BootFactory.createShip(), isHorizontal);
                break;
        }

        for (int i = 0; i < size; i++) {
            int r = isHorizontal ? row : row + i;
            int c = isHorizontal ? col + i : col;
            gridCells[r][c].setBackground(Color.GRAY);
        }

        currentPlayerShipCounts.put(shipType, currentPlayerShipCounts.getOrDefault(shipType, 0) + 1);

        if (getPlacedShipsCount() >= TOTAL_SHIPS) {
            switchToShootingView();

        }
    }

    /**
     * Gets the ship at the specified location.
     * 
     * @param row The row of the location.
     * @param col The column of the location.
     * @return The ship at the specified location, or null if no ship is present.
     */
    public IShip getShipAt(int row, int col) {
        Map<Point, IShip> ships = currentPlayer.getGameBoard().getShipLocations();
        return ships.get(new Point(col, row));
    }

    /**
     * Removes the ship at the specified location.
     * 
     * @param row  The row of the location.
     * @param col  The column of the location.
     * @param ship The ship to be removed.
     */
    public void removeShip(int row, int col, IShip ship, JPanel[][] gridCells) {
        if (ship == null) {
            return;
        }

        currentPlayer.getGameBoard().removeShip(ship);

        int newCount = currentPlayerShipCounts.get(ship.getShipName()) - 1;
        currentPlayerShipCounts.put(ship.getShipName(), newCount);

        placementView.clearGrid();

        Map<Point, IShip> ships = currentPlayer.getGameBoard().getShipLocations();

        for (Map.Entry<Point, IShip> entry : ships.entrySet()) {
            Point point = entry.getKey();
            int r = point.y;
            int c = point.x;
            gridCells[r][c].setBackground(Color.GRAY);
        }
    }

    /**
     * Switches to the shooting view in the GUI.
     */
    private void switchToShootingView() {
        JOptionPane.showMessageDialog(placementView, "Schiffe platziert. Klicke auf OK, um fortzufahren.");
        battleshipGUI.showComputerShootingView();
    }

    /**
     * Checks if a cell is adjacent to any ship.
     * 
     * @param row The row of the cell.
     * @param col The column of the cell.
     * @return True if the cell is adjacent to any ship, false otherwise.
     */
    private boolean isAdjacentToShip(int row, int col) {
        Map<Point, IShip> ships = currentPlayer.getGameBoard().getShipLocations();
        return isAdjacent(row, col, ships);
    }

    /**
     * Checks if a cell is adjacent to any ship in the given map.
     * 
     * @param row   The row of the cell.
     * @param col   The column of the cell.
     * @param ships The map of ship locations.
     * @return True if the cell is adjacent to any ship, false otherwise.
     */
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

    /**
     * Checks if the current player can place a ship of the given type.
     * 
     * @param shipType The type of the ship.
     * @return True if the player can place the ship, false otherwise.
     */
    public boolean canPlaceShip(String shipType) {
        int currentCount = currentPlayerShipCounts.getOrDefault(shipType, 0);
        int maxCount = shipLimits.get(shipType);
        return currentCount < maxCount;
    }

    /**
     * Gets the size of the ship of the given type.
     * 
     * @param shipType The type of the ship.
     * @return The size of the ship.
     */
    public int getShipSize(String shipType) {
        switch (shipType) {
            case "Schlachtschiff":
                return schlachtschiffFactory.getShipSize();
            case "Kreuzer":
                return kreuzerFactory.getShipSize();
            case "Zerstörer":
                return zerstörerFactory.getShipSize();
            case "U-Boot":
                return u_BootFactory.getShipSize();
            default:
                return 0;
        }
    }

    /**
     * Gets the total number of ships placed by the current player.
     * 
     * @return The total number of ships placed.
     */
    public int getPlacedShipsCount() {
        int totalCount = 0;
        for (int count : currentPlayerShipCounts.values()) {
            totalCount += count;
        }
        return totalCount;
    }

}