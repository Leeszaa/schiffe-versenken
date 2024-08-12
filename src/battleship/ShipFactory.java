package battleship;

public interface ShipFactory {
    Ship createShip(int size, boolean isHorizontal);
}

class DefaultShipFactory implements ShipFactory {
    @Override
    public Ship createShip(int size, boolean isHorizontal) {

        return new Ship(size, isHorizontal); 
    }
}
