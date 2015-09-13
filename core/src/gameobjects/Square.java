package gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import gameworld.GameWorld;
import helpers.FlatColors;

public class Square extends GameObject {

    public enum Type {
        sqEmpty, sqWhite, sqRed, sqPurple, sqOrange, sqGreen, sqYellow, sqBlue
    }

    private Type type;
    public int column, row;

    public Square(GameWorld world, float x, float y, float width, float height,
                  TextureRegion texture, Color color, Shape shape, int column, int row, int type) {
        super(world, x, y, width, height, texture, color, shape);
        this.column = column;
        this.row = row;
        this.type = numToType(type);
        FlatColors.organizeColors();
        sprite.setColor(FlatColors.colors.get(type-1));
    }

    public void update(float delta) {
        super.update(delta);
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        super.render(batch, shapeRenderer);
    }

    public void setCoord(int column, int row) {
        this.column = column;
        this.row = row;
    }

    public static Type numToType(int num) {
        switch (num) {
            case 1:
                return Type.sqWhite;
            case 2:
                return Type.sqRed;
            case 3:
                return Type.sqPurple;
            case 4:
                return Type.sqOrange;
            case 5:
                return Type.sqGreen;
            case 6:
                return Type.sqYellow;
            case 7:
                return Type.sqBlue;
            default:
                return Type.sqEmpty;
        }
    }

}
