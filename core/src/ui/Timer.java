package ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import gameobjects.GameObject;
import gameworld.GameWorld;

public class Timer extends GameObject {

    private NinePatch ninepatch;

    public Timer(GameWorld world, float x, float y, float width, float height,
                 Texture texture, Color color,
                 Shape shape) {
        super(world, x, y, width, height, texture, color, shape);
        ninepatch = new NinePatch(texture, 80, 80, 77, 77);

    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    @Override
    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        ninepatch.draw(batch, sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }
}
