package battleship;

public interface PlayerFactory {
    Player createPlayer(ShipFactory shipFactory); 
}

class DefaultPlayerFactory implements PlayerFactory {
    @Override
    public Player createPlayer(ShipFactory shipFactory) {
        Player player = new Player();

        return player;
    }
}