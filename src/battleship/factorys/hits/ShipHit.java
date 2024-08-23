package battleship.factorys.hits;

public class ShipHit implements IHits {

    private boolean isHit;
    private int x;
    private int y;

    @Override
    public boolean isHit() {
        return this.isHit;
    }

    @Override
    public void setHit(boolean isHit) {
        this.isHit = isHit;
    }
}
