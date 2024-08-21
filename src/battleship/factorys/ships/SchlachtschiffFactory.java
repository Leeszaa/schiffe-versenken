/**
 * @file SchlachtschiffFactory.java
 * @brief Factory class for creating battleships in the Battleship game.
 */

package battleship.factorys.ships;

/**
 * @class SchlachtschiffFactory
 * @brief Factory class for creating battleships in the Battleship game.
 * Extends the {@link ShipFactory} class.
 */
public class SchlachtschiffFactory extends ShipFactory {

    /**
     * @brief Gets the size of the battleship.
     * @return The size of the battleship.
     */
    public int getShipSize() {
        return 5;
    }

    /**
     * @brief Gets the limit of the battleship.
     * @return The limit of the battleship.
     */
    public int getShipLimit() {
        return 1;
    }

    /**
     * @brief Creates a new battleship.
     * @return A new instance of {@link Schlachtschiff}.
     */
    @Override
    public IShip createShip() {
        IShip ship = new Schlachtschiff();
        ship.setShipSize(5);
        ship.setShipName("Schlachtschiff");
        ship.setShipLimit(1);
        return ship;
    }
}
