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
 * @class SinglePlacementManager
 *        Handles ship placement for the human player when playing against the
 *        computer.
 *        Enforces placement rules, tracks ship counts, and transitions to the
 *        shooting phase.
 */
public class SinglePlacementManager {
    private IPlayer currentPlayer;
    private Map<String, Integer> currentPlayerShipCounts;
    private final Map<String, Integer> shipLimits;
    private final ZerstörerFactory zerstörerFactory;
    private final SchlachtschiffFactory schlachtschiffFactory;
    private final KreuzerFactory kreuzerFactory;
    private final U_BootFactory u_BootFactory;
    private final SinglePlacementView placementView;
    private final BattleshipGUI battleshipGUI;

    /**
     * Total number of ships to be placed by the human player.
     */
    private static final int TOTAL_SHIPS = 10;

    /**
     * Constructor for SinglePlacementManager.
     * 
     * @param player1       The human player object.
     * @param placementView The SinglePlacementView object for visual updates.
     * @param battleshipGUI Reference to the main BattleshipGUI object.
     */
    public SinglePlacementManager(IPlayer player1, SinglePlacementView placementView,
            BattleshipGUI battleshipGUI) {
        this.currentPlayer = player1;
        this.currentPlayerShipCounts = new HashMap<>();
        this.shipLimits = new HashMap<>();
        this.placementView = placementView;
        this.battleshipGUI = battleshipGUI;

        // Initialize factory objects
        this.zerstörerFactory = new ZerstörerFactory();
        this.schlachtschiffFactory = new SchlachtschiffFactory();
        this.kreuzerFactory = new KreuzerFactory();
        this.u_BootFactory = new U_BootFactory();

        initializeShipLimits();
    }

    /**
     * Initializes the ship limits map, specifying how many of each ship type can be
     * placed.
     */
    private void initializeShipLimits() {
        shipLimits.put("Schlachtschiff", schlachtschiffFactory.getShipLimit());
        shipLimits.put("Kreuzer", kreuzerFactory.getShipLimit());
        shipLimits.put("Zerstörer", zerstörerFactory.getShipLimit());
        shipLimits.put("U-Boot", u_BootFactory.getShipLimit());
    }

    /**
     * Places a ship on the game board for the human player.
     * 
     * @param row          The row coordinate where the ship starts.
     * @param col          The column coordinate where the ship starts.
     * @param size         The size (length) of the ship.
     * @param isHorizontal True for horizontal placement, false for vertical
     *                     placement.
     * @param shipType     The type of ship (e.g., "Schlachtschiff", "Kreuzer",
     *                     etc.).
     * @param gridCells    The 2D array of JPanels representing the grid cells in
     *                     the SinglePlacementView.
     * @throws IllegalArgumentException If the attempted placement is invalid (out
     *                                  of bounds or overlapping).
     */
    public void placeShip(int row, int col, int size, boolean isHorizontal, String shipType, JPanel[][] gridCells) {
        // Validate ship placement: check boundaries and overlap
        for (int i = 0; i < size; i++) {
            int r = isHorizontal ? row : row + i;
            int c = isHorizontal ? col + i : col;

            if (r >= 10 || c >= 10 || isAdjacentToShip(r, c)) {
                throw new IllegalArgumentException("Platzierung ungültig.");
            }
        }

        // Place the ship on the player's game board
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

        // Update the GUI to display the placed ship
        for (int i = 0; i < size; i++) {
            int r = isHorizontal ? row : row + i;
            int c = isHorizontal ? col + i : col;
            gridCells[r][c].setBackground(Color.GRAY);
        }

        // Increment the count of placed ships for the current player
        currentPlayerShipCounts.put(shipType, currentPlayerShipCounts.getOrDefault(shipType, 0) + 1);

        // Check if all ships have been placed
        if (getPlacedShipsCount() >= TOTAL_SHIPS) {
            switchToShootingView();
        }
    }

    /**
     * Returns the IShip object located at the given coordinates on the current
     * player's game board.
     * 
     * @param row The row coordinate.
     * @param col The column coordinate.
     * @return The IShip object at the specified location, or null if no ship is
     *         present.
     */
    public IShip getShipAt(int row, int col) {
        Map<Point, IShip> ships = currentPlayer.getGameBoard().getShipLocations();
        return ships.get(new Point(col, row));
    }

    /**
     * Removes a ship from the human player's game board and updates the GUI.
     * 
     * @param row       The row coordinate of the ship's starting point.
     * @param col       The column coordinate of the ship's starting point.
     * @param ship      The IShip object to be removed.
     * @param gridCells The 2D array of JPanels representing the grid cells.
     */
    public void removeShip(int row, int col, IShip ship, JPanel[][] gridCells) {
        if (ship == null) {
            return; // Nothing to remove
        }

        // Remove the ship from the player's game board
        currentPlayer.getGameBoard().removeShip(ship);

        // Decrement the ship count
        int newCount = currentPlayerShipCounts.get(ship.getShipName()) - 1;
        currentPlayerShipCounts.put(ship.getShipName(), newCount);

        // Clear the grid in the GUI
        placementView.clearGrid();

        // Redraw any remaining ships on the grid
        Map<Point, IShip> ships = currentPlayer.getGameBoard().getShipLocations();
        for (Map.Entry<Point, IShip> entry : ships.entrySet()) {
            Point point = entry.getKey();
            int r = point.y;
            int c = point.x;
            gridCells[r][c].setBackground(Color.GRAY);
        }
    }

    /**
     * Switches to the shooting view (ComputerShootingView) in the BattleshipGUI
     * after the human player has finished placing their ships.
     */
    private void switchToShootingView() {
        JOptionPane.showMessageDialog(placementView, "Schiffe platziert. Klicke auf OK, um fortzufahren.");
        battleshipGUI.showComputerShootingView();
    }

    /**
     * Checks if a cell at the given coordinates is adjacent to any ship on the
     * current player's board.
     * 
     * @param row The row coordinate.
     * @param col The column coordinate.
     * @return True if the cell is adjacent to a ship, false otherwise.
     */
    private boolean isAdjacentToShip(int row, int col) {
        Map<Point, IShip> ships = currentPlayer.getGameBoard().getShipLocations();
        return isAdjacent(row, col, ships);
    }

    /**
     * Helper function to check if a cell is adjacent to any ship in the provided
     * map of ship locations.
     * 
     * @param row   The row coordinate.
     * @param col   The column coordinate.
     * @param ships The map of ship locations.
     * @return True if the cell is adjacent to a ship, false otherwise.
     */
    private boolean isAdjacent(int row, int col, Map<Point, IShip> ships) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // Skip the cell itself
                if (i == 0 && j == 0) {
                    continue;
                }
                int r = row + i;
                int c = col + j;
                // Check if the adjacent cell is within bounds and contains a ship
                if (r >= 0 && r < 10 && c >= 0 && c < 10 && ships.containsKey(new Point(c, r))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the human player can place another ship of the given type, based on
     * placement limits.
     * 
     * @param shipType The type of ship to check (e.g., "Schlachtschiff",
     *                 "Kreuzer").
     * @return True if the player can place the ship, false if the limit for that
     *         ship type has been reached.
     */
    public boolean canPlaceShip(String shipType) {
        int currentCount = currentPlayerShipCounts.getOrDefault(shipType, 0);
        int maxCount = shipLimits.get(shipType);
        return currentCount < maxCount;
    }

    /**
     * Returns the size (length) of the ship with the given type name.
     * 
     * @param shipType The name of the ship type (e.g., "Zerstörer").
     * @return The size (length) of the ship, or 0 if the ship type is invalid.
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
     * Returns the total number of ships that the human player has placed on the
     * board.
     * 
     * @return The total number of placed ships.
     */
    public int getPlacedShipsCount() {
        int totalCount = 0;
        for (int count : currentPlayerShipCounts.values()) {
            totalCount += count;
        }
        return totalCount;
    }
}