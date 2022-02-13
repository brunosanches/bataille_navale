package ensta.model;

import ensta.model.ship.AbstractShip;
import ensta.model.ship.ShipState;

public class Tile {
    private ShipState shipState;
    private Boolean hit;
    // Sugestion: boolean isHit, AbstractShip ship, Coords coords

    public Tile(ShipState shipState, Boolean hit) {
        this.shipState = shipState;
        this.hit = hit;
    }

    public Tile(AbstractShip ship, Boolean hit) {
        this.shipState = new ShipState(ship);
        this.hit = hit;
    }

    public Tile(AbstractShip ship) {

        this(ship, null);
    }

    public Tile(ShipState shipState) {

        this(shipState, null);
    }

    public Tile() {

        this((ShipState) null, null);
    }

    public ShipState getShipState() {
        return shipState;
    }

    public boolean hasShip() {

        return shipState != null;
    }

    public AbstractShip getShip() {

        return shipState.getShip();
    }

    public void putShip(AbstractShip ship) {

        this.shipState = new ShipState(ship);
    }

    public Boolean isHit() {

        return hit;
    }

    public void setHit(boolean hit) {

        this.hit = hit;
    }

}
