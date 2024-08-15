package battleship;

public class Destroyer implements IShip {
    private int size;
    private String name;

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
    
}
