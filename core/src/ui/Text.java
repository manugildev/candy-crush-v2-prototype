package ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import configuration.Configuration;
import gameobjects.GameObject;
import gameworld.GameWorld;
import helpers.FlatColors;

public class Text extends GameObject {
    private final BitmapFont font;
    private Color fontColor;
    private final float distance;
    private String text;
    private int halign;
    private boolean shadow;

    public Text(GameWorld world, float x, float y, float width, float height,
                TextureRegion texture, Color color, String text, BitmapFont font, Color fontColor,
                float distance, int halign) {
        super(world, x, y, width, height, texture, color, Shape.RECTANGLE);
        this.font = font;
        this.text = text;
        this.fontColor = fontColor;
        this.distance = distance;
        this.halign = halign;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer, ShaderProgram fontshader) {
        batch.setShader(fontshader);
        if (shadow) {
            font.setColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, 0.3f);
            font.draw(batch, text, getRectangle().x + 50 + 5,
                    getRectangle().y + getRectangle().height - distance - 5,
                    getRectangle().width - 100,
                    halign,
                    true);

        }
        batch.setColor(Color.WHITE);
        font.setColor(fontColor);
        font.draw(batch, text, getRectangle().x,
                getRectangle().y + getRectangle().height - distance, getRectangle().width,
                halign,
                true);
        font.setColor(Color.WHITE);
        batch.setShader(null);
        if (Configuration.DEBUG) {
            batch.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(FlatColors.DARK_GREEN);
            shapeRenderer.rect(getRectangle().x, getRectangle().y, getRectangle().width,
                    getRectangle().height);
            shapeRenderer.end();
            batch.begin();
        }
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setFontColor(Color color) {
        this.fontColor = color;
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer, ShaderProgram fontShader,
                       ShaderProgram fontShaderA) {


        batch.setShader(fontShader);
        font.setColor(fontColor);
        font.draw(batch, text, getRectangle().x + 50,
                getRectangle().y + getRectangle().height - distance, getRectangle().width - 100,
                halign,
                true);
        font.setColor(Color.WHITE);
        batch.setShader(null);
        if (Configuration.DEBUG) {
            batch.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(FlatColors.DARK_GREEN);
            shapeRenderer.rect(getRectangle().x, getRectangle().y, getRectangle().width, getRectangle().height);
            shapeRenderer.end();
            batch.begin();
        }
    }


    public void setColorText(Color colorText) {
        this.fontColor = colorText;
    }
}
