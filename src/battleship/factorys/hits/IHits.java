package battleship.factorys.hits;

/**
 * @interface IHits
 *   Interface for hits in the Battleship game.
 */
public interface IHits {

    /**
     *   Checks if the hit is successful.
     * @return True if the hit is successful, false otherwise.
     */
    public boolean isHit();

    /**
     *   Sets the hit status.
     * @param isHit True if the hit is successful, false otherwise.
     */
    public void setHit(boolean isHit);

}