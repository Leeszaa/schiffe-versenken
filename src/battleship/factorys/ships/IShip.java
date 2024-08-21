package battleship.factorys.ships;

public interface IShip {
    int getShipSize();
    int getShipLimit();
    String getShipName();
    void setShipSize(int size);
    void setShipName(String name);
    void setShipLimit(int limit);
}