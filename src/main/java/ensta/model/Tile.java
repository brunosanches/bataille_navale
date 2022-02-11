package ensta.model;

public class Tile {
    private char ship;
    private boolean hit;
    // Sugestion: boolean isHit, AbstractShip ship, Coords coords

    public Tile(char ship, boolean hit) {
        this.ship = ship;
        this.hit = hit;
    }

    public Tile(char ship) {
        this(ship, false);
    }

    public Tile() {
        this('.', false);
    }

    public char getShip() {
        return ship;
    }

    public void setShip(char ship) {
        this.ship = ship;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }
}
