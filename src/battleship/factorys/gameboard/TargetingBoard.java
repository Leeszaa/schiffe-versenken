/**
 * @file TargetingBoard.java
 *   Represents a targeting board in the Battleship game.
 */

package battleship.factorys.gameboard;

import java.util.HashMap;
import java.util.Map;
import java.awt.*;
import battleship.factorys.ships.*;
import battleship.factorys.hits.*;

/**
 * @class TargetingBoard
 *        Represents a targeting board in the Battleship game.
 *        Implements the {@link IGameBoard} interface.
 */
public class TargetingBoard implements IGameBoard {
    private final Map<Point, IShip> ships;
    /** < A map of ship locations on the board */
    private final Map<Point, Boolean> hitAttempts;
    /** < A map of hit attempts on the board */
    private final Map<Point, IHits> hits;
    /** < A map of hits on the board */

    /**
     * Constructor for TargetingBoard.
     * Initializes the ships and hitAttempts maps.
     */
    public TargetingBoard() {
        hits = new HashMap<>();
        ships = new HashMap<>();
        hitAttempts = new HashMap<>();
    }

    public void placeHit(int x, int y, IHits hit) {
         Point point = new Point(x, y);
         hits.put(point, hit);
     }

    public Map<Point, IHits> getHits() {
        return new HashMap<>(hits);
    }

    /**
     * Places a ship on the game board.
     * 
     * @param x            The x-coordinate of the starting position.
     * @param y            The y-coordinate of the starting position.
     * @param ship         The ship to be placed.
     * @param isHorizontal True if the ship is placed horizontally, false if
     *                     vertically.
     */
    @Override
    public void placeShip(int x, int y, IShip ship, boolean isHorizontal) {
        // Implementation for placing a ship
    }

    /**
     * Gets the locations of all ships on the board.
     * 
     * @return A map of ship locations.
     */
    @Override
    public Map<Point, IShip> getShipLocations() {
        return new HashMap<>(ships);
    }

    /**
     * Checks if a ship is hit at the given coordinates.
     * 
     * @param x The x-coordinate to check.
     * @param y The y-coordinate to check.
     * @return False as this board does not track ship hits.
     */
    @Override
    public boolean isShipHit(int x, int y) {
        return false;
    }

    /**
     * Gets the hit attempts on the board.
     * 
     * @return A map of hit attempts.
     */
    public Map<Point, Boolean> getHitAttempts() {
        return new HashMap<>(hitAttempts);
    }

    @Override
    public void removeShip(IShip ship) {
        ships.entrySet().removeIf(entry -> entry.getValue().equals(ship));
    }
}