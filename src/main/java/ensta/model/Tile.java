package ensta.model;

import ensta.model.ship.AbstractShip;

public class Tile {
    private AbstractShip ship;
    private boolean hit;
    // Sugestion: boolean isHit, AbstractShip ship, Coords coords

    public Tile(AbstractShip ship, boolean hit) {
        this.ship = ship;
        this.hit = hit;
    }

    public Tile(AbstractShip ship) {
        this(ship, false);
    }

    public Tile() {
        this(null, false);
    }

    public boolean hasShip() { return ship != null; }

    public AbstractShip getShip() {
        return ship;
    }

    public void putShip(AbstractShip ship) {
        this.ship = ship;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

}
