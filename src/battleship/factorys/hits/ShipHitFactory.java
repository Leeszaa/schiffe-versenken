package battleship.factorys.hits;

public class ShipHitFactory extends HitFactory {

    @Override
    public IHits createHit(boolean isHit) {

        IHits hit = new ShipHit();
        hit.setHit(isHit);
        return hit;
    }
}
