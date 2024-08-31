package battleship.factorys.hits;

/**
 * @class HitFactory
 *   Abstract factory class for creating hits in the Battleship game.
 */
public abstract class HitFactory {

    /**
     *   Creates a hit.
     * @param isHit True if the hit is successful, false otherwise.
     * @return An instance of {@link IHits} representing the hit.
     */
    public abstract IHits createHit(boolean isHit);

}
