/**
 * @file U_BootFactory.java
 *   Factory class for creating submarines (U-Boot) in the Battleship game.
 */

package battleship.factorys.ships;

/**
 * @class U_BootFactory
 *   Factory class for creating submarines (U-Boot) in the Battleship game.
 * Extends the {@link ShipFactory} class.
 */
public class U_BootFactory extends ShipFactory {

    /**
     *   Gets the size of the submarine.
     * @return The size of the submarine.
     */

    public int getShipSize() {
        return 2;
    }

    /**
     *   Gets the limit of the submarine.
     * @return The limit of the submarine.
     */

    public int getShipLimit() {
        return 4;
    }

    /**
     *   Creates a new submarine.
     * @return A new instance of {@link U_Boot}.
     */
    @Override
    public IShip createShip() {
        IShip ship = new U_Boot();
        ship.setShipSize(2);
        ship.setShipName("U-Boot");
        ship.setShipLimit(4);
        return ship;
    }
}