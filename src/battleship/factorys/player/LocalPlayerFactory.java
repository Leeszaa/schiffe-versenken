package battleship.factorys.player;

public class LocalPlayerFactory extends PlayerFactory {

    @Override
    public IPlayer createPlayer(String name) {
        return new LocalPlayer(name);
    }
}
