package ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import gameobjects.GameObject;
import gameworld.GameWorld;

public class Timer extends GameObject {

    public Timer(GameWorld world, float x, float y, float width, float height,
                 Texture texture, Color color,
                 Shape shape) {
        super(world, x, y, width, height, texture, color, shape);
    }
}
