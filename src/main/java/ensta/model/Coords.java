package ensta.model;

import java.io.Serializable;
import java.util.Random;

public class Coords implements Serializable {
    private int x;
    private int y;

    public Coords() {
        x = y = 0;
    }

    public Coords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coords(Coords coords) {
        this.x = coords.getX();
        this.y = coords.getY();
    }

    public static Coords randomCoords(int size) {
        Random rand = new Random();
        return new Coords(rand.nextInt(size), rand.nextInt(size));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setCoords(Coords res) {
        x = res.getX();
        y = res.getY();
    }

    public boolean isInBoard(int size) {
        return x >= 0 && x < size && y > 0 && y <= size;
    }

    public void setX(int i) {
        x = i;
    }

    public void setY(int i) {
        y = i;
    }
}
