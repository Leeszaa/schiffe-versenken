package battleship.factorys.hits;

/**
 * @class ShipHit
 *   Represents a hit on a ship in the Battleship game.
 *   Implements the {@link IHits} interface.
 */
public class ShipHit implements IHits {

    private boolean isHit; /**< Indicates if the ship is hit */

    /**
     *   Checks if the ship is hit.
     * @return True if the ship is hit, false otherwise.
     */
    @Override
    public boolean isHit() {
        return this.isHit;
    }

    /**
     *   Sets the hit status of the ship.
     * @param isHit True if the ship is hit, false otherwise.
     */
    @Override
    public void setHit(boolean isHit) {
        this.isHit = isHit;
    }
}
