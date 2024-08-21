/**
 * @file IShip.java
 * @brief Interface for a ship in the Battleship game.
 */

package battleship.factorys.ships;

/**
 * @interface IShip
 * @brief Interface for a ship in the Battleship game.
 */
public interface IShip {

    /**
     * @brief Gets the size of the ship.
     * @return The size of the ship.
     */
    int getShipSize();

    /**
     * @brief Gets the limit of the ship.
     * @return The limit of the ship.
     */
    int getShipLimit();

    /**
     * @brief Gets the name of the ship.
     * @return The name of the ship.
     */
    String getShipName();

    /**
     * @brief Sets the size of the ship.
     * @param size The size to set.
     */
    void setShipSize(int size);

    /**
     * @brief Sets the name of the ship.
     * @param name The name to set.
     */
    void setShipName(String name);

    /**
     * @brief Sets the limit of the ship.
     * @param limit The limit to set.
     */
    void setShipLimit(int limit);
}