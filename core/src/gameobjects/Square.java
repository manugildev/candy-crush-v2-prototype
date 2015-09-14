package gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import configuration.Settings;
import gameworld.GameRenderer;
import gameworld.GameWorld;
import helpers.AssetLoader;
import helpers.FlatColors;
import ui.Text;

public class Square extends GameObject {

    public enum Type {
        sqEmpty, sqWhite, sqRed, sqPurple, sqOrange, sqGreen, sqYellow, sqBlue
    }

    public Type type;
    public int column, row;
    private Text text;

    public boolean isSelected = false;

    public Square(GameWorld world, float x, float y, float width, float height,
                  TextureRegion texture, Color color, Shape shape, int column, int row, int type) {
        super(world, x, y, width, height, texture, color, shape);
        this.column = column;
        this.row = row;
        this.type = numToType(type);
        FlatColors.organizeColors();
        sprite.setColor(FlatColors.colors.get(type - 1));
        text = new Text(world, x + 10, y, width, height, texture, color, column + "" + row,
                AssetLoader.font08, FlatColors.WHITE, 10,
                Align.left);
    }

    public void update(float delta) {
        super.update(delta);
        text.update(delta);
        text.setText(column + "" + row);
        text.setPosition(getPosition().x + 10, getPosition().y);
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        super.render(batch, shapeRenderer);
        //if (Configuration.DEBUG)
        text.render(batch, shapeRenderer, GameRenderer.fontShader);
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

    public void start() {
        float delay = MathUtils.random(0.2f, 0.8f);
        fadeIn(.5f, delay);
        scale(0, 1, .5f, delay);
    }

    public void select() {
        scale(1, 1.1f, .2f, .0f);
        isSelected = true;
    }

    public void slideRight() {
        if (checkValidMovement(column + 1, row)) {
            //Gdx.app.log("Slide Right", column + " " + row + " " + (column + 1) + " " + row);
            swapXandY(world.board.squares, column, row, column + 1, row);
        }
    }


    public void slideLeft() {
        if (checkValidMovement(column - 1, row)) {
            //Gdx.app.log("Slide Left", column + " " + row + " " + (column - 1) + " " + row);
            swapXandY(world.board.squares, column, row, column - 1, row);
        }
    }

    public void slideDown() {
        if (checkValidMovement(column, row - 1)) {
            //Gdx.app.log("Slide Down", column + " " + row + " " + column + " " + (row - 1));
            swapXandY(world.board.squares, column, row, column, row - 1);
        }
    }

    public void slideUp() {
        if (checkValidMovement(column, row + 1)) {
            //Gdx.app.log("Slide Up", column + " " + row + " " + column + " " + (row + 1));
            swapXandY(world.board.squares, column, row, column, row + 1);
        }
    }

    private boolean checkValidMovement(int futureRow, int futureColumn) {

        if (futureRow < 0 || futureRow > Settings.NUM_OF_SQUARES - 1) {
            return false;
        } else if (futureColumn < 0 || futureColumn > Settings.NUM_OF_SQUARES - 1) {
            return false;
        } else {
            return true;
        }
    }

    private void swapXandY(Square[][] squares, int x1, int y1, int x2, int y2) {
        Vector2 tempV = squares[x1][y1].getPosition().cpy();
        squares[x1][y1].setPosition(squares[x2][y2].getPosition().cpy());
        squares[x2][y2].setPosition(tempV);

        Square temp = squares[x1][y1];
        squares[x1][y1] = squares[x2][y2];
        squares[x2][y2] = temp;

        //CHANGING COLUMN AND ROW
        squares[x2][y2].setCoord(x2, y2);
        squares[x1][y1].setCoord(x1, y1);

    }
}
