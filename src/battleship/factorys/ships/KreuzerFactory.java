/**
 * @file KreuzerFactory.java
 *   Factory class for creating cruisers in the Battleship game.
 */

package battleship.factorys.ships;

/**
 * @class KreuzerFactory
 *   Factory class for creating cruisers in the Battleship game.
 * Extends the {@link ShipFactory} class.
 */
public class KreuzerFactory extends ShipFactory {

    /**
     *   Gets the size of the cruiser.
     * @return The size of the cruiser.
     */
    public int getShipSize() {
        return 4;
    }

    /**
     *   Gets the limit of the cruiser.
     * @return The limit of the cruiser.
     */
    public int getShipLimit() {
        return 2;
    }

    /**
     *   Creates a new cruiser.
     * @return A new instance of {@link Kreuzer}.
     */
    @Override
    public IShip createShip() {
        IShip ship = new Kreuzer();
        ship.setShipSize(4);
        ship.setShipName("Kreuzer");
        ship.setShipLimit(2);
        return ship;
    }
}