package battleship;

public interface ShipFactory {
    Ship createSchlachtschiff();
    Ship createKreuzer();
    Ship createZerstörer();
    Ship createUBoot();
}

class DefaultShipFactory implements ShipFactory {
    @Override
    public Ship createSchlachtschiff() {
        return new Ship(5, false); // Länge 5, zunächst vertikal
    }

    @Override
    public Ship createKreuzer() {
        return new Ship(4, false); // Länge 4, zunächst vertikal
    }

    @Override
    public Ship createZerstörer() {
        return new Ship(3, false); // Länge 3, zunächst vertikal
    }

    @Override
    public Ship createUBoot() {
        return new Ship(2, false); // Länge 2, zunächst vertikal
    }
}