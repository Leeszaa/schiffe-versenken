/**
 * @file PlayerFactory.java
 *   Abstract factory class for creating players in the Battleship game.
 */

package battleship.factorys.player;

/**
 * @class PlayerFactory
 *   Abstract factory class for creating players in the Battleship game.
 */
public abstract class PlayerFactory {

    /**
     *   Creates a player with the given name.
     * @param name The name of the player.
     * @return A new instance of {@link IPlayer}.
     */
    public abstract IPlayer createPlayer(String name);
}