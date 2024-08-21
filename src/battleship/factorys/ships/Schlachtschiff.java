/**
 * @file Schlachtschiff.java
 *   Implementation of the IShip interface for a battleship in the Battleship game.
 */

package battleship.factorys.ships;

/**
 * @class Schlachtschiff
 *   Represents a battleship in the Battleship game.
 * Implements the {@link IShip} interface.
 */
public class Schlachtschiff implements IShip {
    private int size; /**< The size of the ship */
    private String name; /**< The name of the ship */
    private int limit; /**< The limit of the ship */

    /**
     *   Gets the size of the ship.
     * @return The size of the ship.
     */
    @Override
    public int getShipSize() {
        return size;
    }

    /**
     *   Gets the name of the ship.
     * @return The name of the ship.
     */
    @Override
    public String getShipName() {
        return name;
    }

    /**
     *   Sets the size of the ship.
     * @param size The size to set.
     */
    @Override
    public void setShipSize(int size) {
        this.size = size;
    }

    /**
     *   Sets the name of the ship.
     * @param name The name to set.
     */
    @Override
    public void setShipName(String name) {
        this.name = name;
    }

    /**
     *   Gets the limit of the ship.
     * @return The limit of the ship.
     */
    @Override
    public int getShipLimit() {
        return limit;
    }

    /**
     *   Sets the limit of the ship.
     * @param limit The limit to set.
     */
    @Override
    public void setShipLimit(int limit) {
        this.limit = limit;
    }
}