package battleship.factorys.ships;

public class KreuzerFactory extends ShipFactory {

    public int getShipSize() {
        return 4;
    }

    public int getShipLimit() {
        return 2;
    }

    public IShip createShip() {
        IShip ship = new Kreuzer();
        ship.setShipSize(4);
        ship.setShipName("Kreuzer");
        ship.setShipLimit(2);
        return ship;
    }
}
