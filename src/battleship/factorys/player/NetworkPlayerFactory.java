/**
 * @file NetworkPlayerFactory.java
 * @brief Factory class for creating network players in the Battleship game.
 */

package battleship.factorys.player;

/**
 * @class NetworkPlayerFactory
 * @brief Factory class for creating network players in the Battleship game.
 * Extends the {@link PlayerFactory} class.
 */
public class NetworkPlayerFactory extends PlayerFactory {

    /**
     * @brief Creates a network player with the given name.
     * @param name The name of the player.
     * @return A new instance of {@link NetworkPlayer}.
     */
    @Override
    public IPlayer createPlayer(String name) {
        return new NetworkPlayer(name);
    }
}