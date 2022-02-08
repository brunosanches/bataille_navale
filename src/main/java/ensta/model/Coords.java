package ensta.model;

public class Coords {
    private int x;
    private int y;

    // TODO: Implement Coords constructors

    public Coords() {
        x = y = 0;
    }

    public Coords(int x, int iy) {
        new Coords();
    }

    public Coords(Coords coords) {

    }

    // TODO: Implement randomCoords
    public static Coords randomCoords(int size) {
        return new Coords();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // TODO: Implement setCoords receiving coords from other Coords object
    public void setCoords(Coords res) {

    }

    // TODO: Implement isInBoard
    public boolean isInBoard(int size) {
        return false;
    }

    public void setX(int i) {
    }

    public void setY(int i) {
    }
}
