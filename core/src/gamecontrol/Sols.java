package gamecontrol;

public class Sols {
    public int d;
    public int x;
    public int y;

    public Sols() {
        x = -1;
        y = -1;
        d = -1;
    }

    public Sols(int x, int y, int d) {
        this.x = x;
        this.y = y;
        this.d = d;
    }

    public String toString() {
        return "{x: " + x + ", y: " + y + ", d: " + d + "}";
    }
}