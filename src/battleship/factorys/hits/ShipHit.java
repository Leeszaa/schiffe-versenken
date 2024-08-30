package battleship.factorys.hits;

public class ShipHit implements IHits {

    private boolean isHit;
    
    @Override
    public boolean isHit() {
        return this.isHit;
    }

    @Override
    public void setHit(boolean isHit) {
        this.isHit = isHit;
    }
}
