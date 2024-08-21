package battleship.factorys.ships;

public class U_Boot implements IShip {
    private int size;
    private String name;
    private int limit;

    @Override
    public int getShipSize() {
        return size;
    }

    @Override
    public String getShipName() {
        return name;
    }

    @Override
    public void setShipSize(int size) {
        this.size = size;
    }

    @Override
    public void setShipName(String name) {
        this.name = name;
    }
    
    @Override
    public int getShipLimit() {
        return limit;
    }

    @Override
    public void setShipLimit(int limit) {
        this.limit = limit;
    }
}
