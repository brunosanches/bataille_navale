package ensta.model.ship;

import ensta.util.Orientation;

import java.io.Serializable;

public abstract class AbstractShip implements Serializable {
    private char label;
    private String name;
    private int length;
    private Orientation orientation;
    private int strikeCount = 0;

    public AbstractShip(String name, char label, int length, Orientation orientation) {
        this.name = name;
        this.label = label;
        this.length = length;
        this.orientation = orientation;
    }

    public boolean isSunk() {
        return strikeCount == length;
    }

    public void addStrike() { strikeCount++; }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) { this.orientation = orientation; }

    public int getLength() {
        return length;
    }

    public Object getName() {
        return name;
    }

    public char getLabel() { return label; }



}
