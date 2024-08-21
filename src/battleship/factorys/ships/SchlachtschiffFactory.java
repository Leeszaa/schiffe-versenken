package battleship.factorys.ships;

public class SchlachtschiffFactory extends ShipFactory {
    public int getShipSize() {
        return 5;
    }

    public int getShipLimit() {
        return 1;
    }

    public IShip createShip() {
        IShip ship = new Schlachtschiff();
        ship.setShipSize(5);
        ship.setShipName("Schlachtschiff");
        ship.setShipLimit(1);
        return ship;
    }
}
