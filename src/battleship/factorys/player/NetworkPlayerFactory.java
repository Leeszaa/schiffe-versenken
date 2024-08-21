package battleship.factorys.player;

public class NetworkPlayerFactory extends PlayerFactory {
    @Override
    public IPlayer createPlayer(String name) {
        return new NetworkPlayer(name);
    }
}