package helpers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

public class FlatColors {

    public static final Array<Color> colors = new Array<Color>();

    public static final Color RED = parseColor("#e74c3c", 1f);
    public static final Color DARK_RED = parseColor("#c0392b", 1f);
    public static final Color EVEN_DARK_RED = parseColor("89211e");

    public static final Color GREEN = parseColor("#2ecc71", 1f);
    public static final Color DARK_GREEN = parseColor("#27ae60", 1f);

    public static final Color BLUE = parseColor("#3498db", 1f);
    public static final Color DARK_BLUE = parseColor("#2980b9", 1f);

    public static final Color ORANGE = parseColor("#e67e22", 1f);
    public static final Color DARK_ORANGE = parseColor("#d35400", 1f);

    public static final Color GREY = parseColor("#95a5a6", 1f);
    public static final Color DARK_GREY = parseColor("#7f8c8d", 1f);

    public static final Color PURPLE = parseColor("#9b59b6", 1f);
    public static final Color DARK_PURPLE = parseColor("#8e44ad", 1f);

    public static final Color YELLOW = parseColor("#f1c40f", 1f);
    public static final Color DARK_YELLOW = parseColor("#f39c12", 1f);

    public static final Color BLACK = parseColor("#34495e", 1f);
    public static final Color DARK_BLACK = parseColor("#2c3e50", 1f);
    public static final Color LIGHT_BLACK = parseColor("#445d78", 1f);

    public static final Color WHITE = parseColor("#ecf0f1", 1f);
    public static final Color DARK_WHITE = parseColor("#bdc3c7", 1f);

    public static final Color SEA = parseColor("#1abc9c", 1f);
    public static final Color DARK_SEA = parseColor("#16a085", 1f);

    public static final Color SKY_BLUE = parseColor("#242d58", 1f);
    public static final Color DARK_SKY_BLUE = parseColor("#181f3f", 1f);
    public static final Color LOGO_GREEN = parseColor("00A386");

    public static Color parseColor(String hex, float alpha) {
        String hex1 = hex;
        if (hex1.indexOf("#") != -1) {
            hex1 = hex1.substring(1);
        }
        Color color = Color.valueOf(hex1);
        color.a = alpha;
        return color;
    }

    public static Color parseColor(String hex) {
        String hex1 = hex;
        if (hex1.indexOf("#") != -1) {
            hex1 = hex1.substring(1);
        }
        Color color = Color.valueOf(hex1);
        color.a = 1f;
        return color;
    }

    public static void organizeColors() {
        colors.addAll(RED, DARK_RED, DARK_GREEN, DARK_BLUE, DARK_YELLOW,
                DARK_PURPLE, PURPLE, YELLOW, GREEN, SEA, DARK_SEA,
                BLUE, ORANGE, DARK_ORANGE);
    }
}
