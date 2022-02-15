package ensta.model.ship;

import ensta.util.ColorUtil;

import java.io.Serializable;

public class ShipState implements Serializable {
    private AbstractShip ship;
    private boolean struck;

    public ShipState(AbstractShip ship, boolean struck) {
        this.ship = ship;
        this.struck = struck;
    }

    public ShipState(AbstractShip ship) {
        this(ship, false);
    }

    public ShipState() {
        this(null, false);
    }

    public void addStrike() {
        if (!struck) {
            ship.addStrike();
            struck = true;
        }
    }

    public boolean isStruck() {
        return struck;
    }

    public String toString() {
        if (struck) {
            return ColorUtil.colorize(ship.getLabel(), ColorUtil.Color.RED);
        }
        return String.valueOf(ship.getLabel());
    }

    public boolean isSunk() {
        return ship.isSunk();
    }

    public AbstractShip getShip() {
        return ship;
    }

}
