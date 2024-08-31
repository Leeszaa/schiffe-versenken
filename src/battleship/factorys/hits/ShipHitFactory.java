package battleship.factorys.hits;

/**
 * @class ShipHitFactory
 *   Factory class for creating ship hits in the Battleship game.
 *   Extends the {@link HitFactory} class.
 */
public class ShipHitFactory extends HitFactory {

    /**
     *   Creates a hit on a ship.
     * @param isHit True if the hit is successful, false otherwise.
     * @return An instance of {@link IHits} representing the hit.
     */
    @Override
    public IHits createHit(boolean isHit) {
        IHits hit = new ShipHit();
        hit.setHit(isHit);
        return hit;
    }
}