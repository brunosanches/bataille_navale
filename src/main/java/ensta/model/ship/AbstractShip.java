package ensta.model.ship;

import ensta.util.Orientation;

public abstract class AbstractShip {
    // TODO: Implement isSunk()
    public boolean isSunk() {
        return false;
    }

    // TODO: Implement getOrientation()
    public Orientation getOrientation() {
        return Orientation.EAST;
    }

    // TODO: Implement getLength()
    public int getLength() {
        return 0;
    }

    // TODO: Implement getName()
    public Object getName() {
        return null;
    }
}
