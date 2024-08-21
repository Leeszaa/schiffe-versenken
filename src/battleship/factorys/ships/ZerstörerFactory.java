package battleship.factorys.ships;

public class ZerstörerFactory extends ShipFactory {

    public int getShipSize() {
        return 3;
    }

    public int getShipLimit() {
        return 3;
    }

    public IShip createShip() {
        IShip ship = new Zerstörer();
        ship.setShipSize(2);
        ship.setShipName("Destroyer");
        ship.setShipLimit(3);
        return ship;
    }
}
