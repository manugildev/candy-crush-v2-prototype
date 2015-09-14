package gamecontrol;

public class Coord {
    public int x;
    public int y;

    public Coord() {
        x = -1;
        y = -1;
    }

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "{x: " + x + ", y: " + y + "}";
    }
}
