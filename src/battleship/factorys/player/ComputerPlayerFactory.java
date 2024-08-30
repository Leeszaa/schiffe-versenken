package battleship.factorys.player;

public class ComputerPlayerFactory extends PlayerFactory {

    /**
     *   Creates a local player with the given name.
     * @param name The name of the player.
     * @return A new instance of {@link LocalPlayer}.
     */
    @Override
    public IPlayer createPlayer(String name) {
        return new ComputerPlayer(name);
    }
}
