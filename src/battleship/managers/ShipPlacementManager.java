package battleship.managers;

import battleship.BattleshipGUI;
import battleship.factorys.player.IPlayer;
import battleship.factorys.ships.*;
import battleship.views.PlacementView;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @class ShipPlacementManager
 *        Handles the placement of ships on the game board for both players.
 *        Enforces placement rules and manages ship counts.
 */
public class ShipPlacementManager {
    private IPlayer currentPlayer;
    private final IPlayer player1;
    private final IPlayer player2;
    private Map<String, Integer> currentPlayerShipCounts;
    private final Map<String, Integer> shipLimits;
    private final ZerstörerFactory zerstörerFactory;
    private final SchlachtschiffFactory schlachtschiffFactory;
    private final KreuzerFactory kreuzerFactory;
    private final U_BootFactory u_BootFactory;
    private final PlacementView placementView;
    private final BattleshipGUI battleshipGUI;

    /**
     * Total number of ships to be placed by each player.
     */
    private static final int TOTAL_SHIPS = 10;

    /**
     * Constructor for ShipPlacementManager.
     * 
     * @param player1       The first player.
     * @param player2       The second player.
     * @param placementView The PlacementView object to use for visual updates.
     * @param battleshipGUI Reference to the main BattleshipGUI object.
     */
    public ShipPlacementManager(IPlayer player1, IPlayer player2, PlacementView placementView,
            BattleshipGUI battleshipGUI) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;
        this.currentPlayerShipCounts = new HashMap<>();
        this.shipLimits = new HashMap<>();
        this.placementView = placementView;
        this.battleshipGUI = battleshipGUI;

        this.zerstörerFactory = new ZerstörerFactory();
        this.schlachtschiffFactory = new SchlachtschiffFactory();
        this.kreuzerFactory = new KreuzerFactory();
        this.u_BootFactory = new U_BootFactory();

        initializeShipLimits();
    }

    /**
     * Initializes the map of ship limits for each ship type.
     */
    private void initializeShipLimits() {
        shipLimits.put("Schlachtschiff", schlachtschiffFactory.getShipLimit());
        shipLimits.put("Kreuzer", kreuzerFactory.getShipLimit());
        shipLimits.put("Zerstörer", zerstörerFactory.getShipLimit());
        shipLimits.put("U-Boot", u_BootFactory.getShipLimit());
    }

    /**
     * Places a ship on the game board for the current player.
     * 
     * @param row          The row coordinate for the starting point of the ship.
     * @param col          The column coordinate for the starting point of the ship.
     * @param size         The size (length) of the ship.
     * @param isHorizontal True if the ship should be placed horizontally, false if
     *                     vertically.
     * @param shipType     The type of ship to place (e.g., "Schlachtschiff").
     * @param gridCells    The 2D array of JPanel representing the grid cells on the
     *                     PlacementView.
     * @throws IllegalArgumentException If the ship placement is invalid (out of
     *                                  bounds or overlapping).
     */
    public void placeShip(int row, int col, int size, boolean isHorizontal, String shipType, JPanel[][] gridCells) {
        // Check for valid placement (not out of bounds and not overlapping other ships)
        for (int i = 0; i < size; i++) {
            int r = isHorizontal ? row : row + i;
            int c = isHorizontal ? col + i : col;

            if (r >= 10 || c >= 10 || isAdjacentToShip(r, c)) {
                throw new IllegalArgumentException("Platzierung ungültig.");
            }
        }

        // Place the ship using the appropriate factory
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

        // Update the GUI to show the placed ship
        for (int i = 0; i < size; i++) {
            int r = isHorizontal ? row : row + i;
            int c = isHorizontal ? col + i : col;
            gridCells[r][c].setBackground(Color.GRAY);
        }

        // Update the ship count for the current player
        currentPlayerShipCounts.put(shipType, currentPlayerShipCounts.getOrDefault(shipType, 0) + 1);

        // Check if all ships have been placed
        if (getPlacedShipsCount() >= TOTAL_SHIPS) {
            if (currentPlayer == player1) {
                // Switch to Player 2's turn
                showConfirmationDialog("Spieler 1 hat alle Schiffe platziert. Jetzt ist Spieler 2 drann.");
            } else {
                // Both players have placed their ships, switch to the shooting phase
                switchToShootingView();
            }
        }
    }

    /**
     * Returns the IShip object located at the specified grid coordinates for the
     * current player.
     * 
     * @param row The row coordinate of the cell.
     * @param col The column coordinate of the cell.
     * @return The IShip at the given location, or null if no ship is present.
     */
    public IShip getShipAt(int row, int col) {
        Map<Point, IShip> ships = currentPlayer.getGameBoard().getShipLocations();
        return ships.get(new Point(col, row));
    }

    /**
     * Removes a ship from the current player's game board and updates the GUI.
     * 
     * @param row       The row coordinate of the ship's starting point.
     * @param col       The column coordinate of the ship's starting point.
     * @param ship      The IShip object to be removed.
     * @param gridCells The 2D array of JPanels representing the grid cells.
     */
    public void removeShip(int row, int col, IShip ship, JPanel[][] gridCells) {
        if (ship == null) {
            return;
        }

        currentPlayer.getGameBoard().removeShip(ship);

        String shipName = ship.getShipName();
        int currentCount = currentPlayerShipCounts.getOrDefault(shipName, 0);
        int newCount = currentCount - 1;
        currentPlayerShipCounts.put(shipName, newCount);

        placementView.clearGrid();

        // Redraw the remaining ships on the grid
        Map<Point, IShip> ships = currentPlayer.getGameBoard().getShipLocations();
        for (Map.Entry<Point, IShip> entry : ships.entrySet()) {
            Point point = entry.getKey();
            int r = point.y;
            int c = point.x;
            gridCells[r][c].setBackground(Color.GRAY);
        }
    }

    /**
     * Switches the current player and updates the GUI to reflect the change.
     */
    private void switchPlayer() {
        placementView.clearGrid();
        if (currentPlayer == player1) {
            currentPlayer = player2;
            currentPlayerShipCounts.clear();
        } else {
            currentPlayer = player1;
            currentPlayerShipCounts.clear();
        }
        placementView.updateLabels();
    }

    /**
     * Displays a confirmation dialog with the specified message.
     * 
     * @param message The message to be displayed in the dialog.
     */
    private void showConfirmationDialog(String message) {
        JOptionPane.showMessageDialog(placementView, message);
        switchPlayer();
    }

    /**
     * Switches the game to the shooting view (ShootingView) in the BattleshipGUI.
     */
    private void switchToShootingView() {
        battleshipGUI.showShootingView(false);
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
     * Checks if a cell at the given coordinates is adjacent to any ship in the
     * provided map.
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
                // Check bounds and if a ship exists at the adjacent cell
                if (r >= 0 && r < 10 && c >= 0 && c < 10 && ships.containsKey(new Point(c, r))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the current player can place another ship of the given type,
     * based on the ship placement limits.
     * 
     * @param shipType The type of ship to check (e.g., "Schlachtschiff").
     * @return True if the player can place the ship, false if the limit has been
     *         reached.
     */
    public boolean canPlaceShip(String shipType) {
        int currentCount = currentPlayerShipCounts.getOrDefault(shipType, 0);
        int maxCount = shipLimits.get(shipType);
        return currentCount < maxCount;
    }

    /**
     * Returns the size (length) of the ship with the given type.
     * 
     * @param shipType The type of ship (e.g., "Kreuzer").
     * @return The size of the ship, or 0 if the ship type is invalid.
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
     * Returns the total number of ships placed by the current player.
     * 
     * @return The total ship count.
     */
    public int getPlacedShipsCount() {
        int totalCount = 0;
        for (int count : currentPlayerShipCounts.values()) {
            totalCount += count;
        }
        return totalCount;
    }

    /**
     * Returns a map of the ship locations for player 1.
     * 
     * @return A Map where the key is the Point representing the ship's starting
     *         location on the grid,
     *         and the value is the IShip object.
     */
    public Map<Point, IShip> getPlayer1ShipLocations() {
        return player1.getGameBoard().getShipLocations();
    }

    /**
     * Returns a map of the ship locations for player 2.
     * 
     * @return A Map where the key is the Point representing the ship's starting
     *         location on the grid,
     *         and the value is the IShip object.
     */
    public Map<Point, IShip> getPlayer2ShipLocations() {
        return player2.getGameBoard().getShipLocations();
    }

    /**
     * Returns a string identifier for the current player.
     * 
     * @return "player1" if the current player is player1, "player2" otherwise.
     */
    public String getCurrentPlayer() {
        return currentPlayer == player1 ? "player1" : "player2";
    }
}
