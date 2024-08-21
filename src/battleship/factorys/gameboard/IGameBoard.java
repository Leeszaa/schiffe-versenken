/**
 * @file IGameBoard.java
 *   Interface for a game board in the Battleship game.
 */

package battleship.factorys.gameboard;

import battleship.factorys.ships.IShip;

import java.awt.*;
import java.util.Map;

/**
 * @interface IGameBoard
 *   Interface for a game board in the Battleship game.
 */
public interface IGameBoard {

    /**
     *   Places a ship on the game board.
     * @param x The x-coordinate where the ship will be placed.
     * @param y The y-coordinate where the ship will be placed.
     * @param ship The ship to be placed.
     * @param isHorizontal True if the ship is placed horizontally, false if vertically.
     */
    void placeShip(int x, int y, IShip ship, boolean isHorizontal);

    /**
     *   Records a hit on the game board.
     * @param x The x-coordinate of the hit.
     * @param y The y-coordinate of the hit.
     * @param isHit True if the hit was successful, false otherwise.
     */
    void receiveHit(int x, int y, boolean isHit);

    /**
     *   Displays the game board.
     */
    void display();

    /**
     *   Gets the locations of all ships on the game board.
     * @return A map of ship locations with coordinates as keys and ships as values.
     */
    Map<Point, IShip> getShipLocations();

    /**
     *   Checks if a ship is hit at the given coordinates.
     * @param x The x-coordinate to check.
     * @param y The y-coordinate to check.
     * @return True if a ship is hit, false otherwise.
     */
    boolean isShipHit(int x, int y);
}