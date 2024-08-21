/**
 * @file LocalPlayerFactory.java
 *   Factory class for creating local players in the Battleship game.
 */

package battleship.factorys.player;

/**
 * @class LocalPlayerFactory
 *   Factory class for creating local players in the Battleship game.
 * Extends the {@link PlayerFactory} class.
 */
public class LocalPlayerFactory extends PlayerFactory {

    /**
     *   Creates a local player with the given name.
     * @param name The name of the player.
     * @return A new instance of {@link LocalPlayer}.
     */
    @Override
    public IPlayer createPlayer(String name) {
        return new LocalPlayer(name);
    }
}