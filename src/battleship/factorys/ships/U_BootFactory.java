package battleship.factorys.ships;

public class U_BootFactory extends ShipFactory {

    public int getShipSize() {
        return 2;
    }

    public int getShipLimit() {
        return 4;
    }

    public IShip createShip() {
        IShip ship = new U_Boot();
        ship.setShipSize(2);
        ship.setShipName("U-Boot");
        ship.setShipLimit(4);
        return ship;
    }
}
