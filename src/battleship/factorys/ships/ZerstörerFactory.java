/**
 * @file ZerstörerFactory.java
 *   Factory class for creating destroyers (Zerstörer) in the Battleship game.
 */

package battleship.factorys.ships;

/**
 * @class ZerstörerFactory
 *   Factory class for creating destroyers (Zerstörer) in the Battleship game.
 * Extends the {@link ShipFactory} class.
 */
public class ZerstörerFactory extends ShipFactory {

    /**
     *   Gets the size of the destroyer.
     * @return The size of the destroyer.
     */
    public int getShipSize() {
        return 3;
    }

    /**
     *   Gets the limit of the destroyer.
     * @return The limit of the destroyer.
     */
    public int getShipLimit() {
        return 3;
    }

    /**
     *   Creates a new destroyer.
     * @return A new instance of {@link Zerstörer}.
     */
    @Override
    public IShip createShip() {
        IShip ship = new Zerstörer();
        ship.setShipSize(3);
        ship.setShipName("Destroyer");
        ship.setShipLimit(3);
        return ship;
    }
}