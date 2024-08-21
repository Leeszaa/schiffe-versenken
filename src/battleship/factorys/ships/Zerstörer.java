/**
 * @file Zerstörer.java
 * @brief Implementation of the IShip interface for a destroyer in the Battleship game.
 */

package battleship.factorys.ships;

/**
 * @class Zerstörer
 * @brief Represents a destroyer in the Battleship game.
 * Implements the {@link IShip} interface.
 */
public class Zerstörer implements IShip {
    private int size; /**< The size of the ship */
    private String name; /**< The name of the ship */
    private int limit; /**< The limit of the ship */

    /**
     * @brief Gets the size of the ship.
     * @return The size of the ship.
     */
    @Override
    public int getShipSize() {
        return size;
    }

    /**
     * @brief Gets the name of the ship.
     * @return The name of the ship.
     */
    @Override
    public String getShipName() {
        return name;
    }

    /**
     * @brief Sets the size of the ship.
     * @param size The size to set.
     */
    @Override
    public void setShipSize(int size) {
        this.size = size;
    }

    /**
     * @brief Sets the name of the ship.
     * @param name The name to set.
     */
    @Override
    public void setShipName(String name) {
        this.name = name;
    }

    /**
     * @brief Gets the limit of the ship.
     * @return The limit of the ship.
     */
    @Override
    public int getShipLimit() {
        return limit;
    }

    /**
     * @brief Sets the limit of the ship.
     * @param limit The limit to set.
     */
    @Override
    public void setShipLimit(int limit) {
        this.limit = limit;
    }
}