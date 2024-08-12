package battleship;

public interface ShipFactory {
    Ship createShip(int size, boolean isHorizontal);
}

class DefaultShipFactory implements ShipFactory {
    @Override
    public Ship createShip(int size, boolean isHorizontal) {
        // Hier kannst du später spezielle Schiffstypen erstellen, z.B. U-Boote mit Sonderfähigkeiten
        return new Ship(size, isHorizontal); 
    }
}
