package battleship;

public interface PlayerFactory {
    Player createPlayer(ShipFactory shipFactory); 
}

class DefaultPlayerFactory implements PlayerFactory {
    @Override
    public Player createPlayer(ShipFactory shipFactory) {
        Player player = new Player();
        // Hier könntest du z.B. die Schiffe direkt beim Spieler erstellen lassen
        // player.placeShip(shipFactory.createShip(5, true), ...);
        return player;
    }
}