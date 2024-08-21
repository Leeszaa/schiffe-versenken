/**
 * @file IShip.java
 *   Interface for a ship in the Battleship game.
 */

package battleship.factorys.ships;

/**
 * @interface IShip
 *   Interface for a ship in the Battleship game.
 */
public interface IShip {

    /**
     *   Gets the size of the ship.
     * @return The size of the ship.
     */
    int getShipSize();

    /**
     *   Gets the limit of the ship.
     * @return The limit of the ship.
     */
    int getShipLimit();

    /**
     *   Gets the name of the ship.
     * @return The name of the ship.
     */
    String getShipName();

    /**
     *   Sets the size of the ship.
     * @param size The size to set.
     */
    void setShipSize(int size);

    /**
     *   Sets the name of the ship.
     * @param name The name to set.
     */
    void setShipName(String name);

    /**
     *   Sets the limit of the ship.
     * @param limit The limit to set.
     */
    void setShipLimit(int limit);

    boolean isHorizontal();
}