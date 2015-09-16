package gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import configuration.Settings;
import gameworld.GameWorld;
import helpers.AssetLoader;
import helpers.FlatColors;
import tweens.Value;
import tweens.VectorAccessor;
import ui.Text;

public class Square extends GameObject {


    public enum Type {
        sqEmpty, sqWhite, sqRed, sqPurple, sqOrange, sqGreen, sqYellow, sqBlue
    }

    public Type type;
    public int column, row;
    private Text text;
    public int typeN, emptyB = 0, emptyA = 0;
    public float diffY = 0;

    public boolean isSelected = false;

    public Square(GameWorld world, float x, float y, float width, float height,
                  TextureRegion texture, Color color, Shape shape, int column, int row, int typeN) {
        super(world, x, y, width, height, texture, color, shape);
        this.column = column;
        this.row = row;
        this.type = numToType(typeN);
        this.typeN = typeN;
        FlatColors.organizeColors();
        setType(typeN);
        text = new Text(world, x + 10, y, width, height, texture, color, column + "" + row,
                AssetLoader.font08, FlatColors.WHITE, 10,
                Align.left);
    }

    public Square(GameWorld world) {
        super(world, 0, 0, 0, 0, AssetLoader.square, FlatColors.WHITE, Shape.RECTANGLE);

    }

    public void update(float delta) {
        super.update(delta);
        text.update(delta);
        text.setText(column + "" + row /*+ "\nE:" + emptyB*/);

        //text.setText(column + "" + row + "\nE:" + emptyA);


        //text.setText(column + "" + row + "\n" + diffY);
        text.setPosition(getPosition().x + 10, getPosition().y);
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        super.render(batch, shapeRenderer);
        //if (Configuration.DEBUG)
        // if (sprite.getScaleX() == 1) text.render(batch, shapeRenderer, GameRenderer.fontShader);
    }

    public void setCoord(int column, int row) {
        this.column = column;
        this.row = row;
    }

    public Type numToType(int num) {
        FlatColors.organizeColors();
        typeN = num;
        switch (num) {
            case 1:
                sprite.setColor(FlatColors.colors.get(num - 1));
                return Type.sqWhite;
            case 2:
                sprite.setColor(FlatColors.colors.get(num - 1));
                return Type.sqRed;
            case 3:
                sprite.setColor(FlatColors.colors.get(num - 1));
                return Type.sqPurple;
            case 4:
                sprite.setColor(FlatColors.colors.get(num - 1));
                return Type.sqOrange;
            case 5:
                sprite.setColor(FlatColors.colors.get(num - 1));
                return Type.sqGreen;
            case 6:
                sprite.setColor(FlatColors.colors.get(num - 1));
                return Type.sqYellow;
            case 7:
                sprite.setColor(FlatColors.colors.get(num - 1));
                return Type.sqBlue;
            default:
                //sprite.setColor(FlatColors.GREY);
                return Type.sqEmpty;


        }
    }

    public void start(float v) {
        fadeIn(.5f, v);
        scale(0, 1, .5f, v);
    }

    public void select() {
        scale(1, 1.1f, .2f, .0f);
        isSelected = true;
    }

    public void slideRight() {
        if (checkValidMovement(column + 1, row)) {
            //Gdx.app.log("Slide Right", column + " " + row + " " + (column + 1) + " " + row);
            swapXandYwithCheck(world.board.squares, column, row, column + 1, row);
        }
    }


    public void slideLeft() {
        if (checkValidMovement(column - 1, row)) {
            //Gdx.app.log("Slide Left", column + " " + row + " " + (column - 1) + " " + row);
            swapXandYwithCheck(world.board.squares, column, row, column - 1, row);
        }
    }

    public void slideDown() {
        if (checkValidMovement(column, row - 1)) {
            //Gdx.app.log("Slide Down", column + " " + row + " " + column + " " + (row - 1));
            swapXandYwithCheck(world.board.squares, column, row, column, row - 1);
        }
    }

    public void slideUp() {
        if (checkValidMovement(column, row + 1)) {
            //Gdx.app.log("Slide Up", column + " " + row + " " + column + " " + (row + 1));
            swapXandYwithCheck(world.board.squares, column, row, column, row + 1);
        }
    }

    private boolean checkValidMovement(int futureRow, int futureColumn) {
        //Gdx.app.log(" Slide", column + " " + row + " " + futureColumn + " " + futureRow);
        if (futureRow < 0 || futureRow > Settings.NUM_OF_SQUARES - 1) {
            return false;
        } else if (futureColumn < 0 || futureColumn > Settings.NUM_OF_SQUARES - 1) {
            return false;
        } else {
            return true;
        }
    }

    public void swapXandY(Square[][] squares, int x1, int y1, int x2, int y2) {
        Vector2 tempV = squares[x1][y1].getPosition().cpy();
        Vector2 tempV1 = squares[x2][y2].getPosition().cpy();
        //squares[x1][y1].setPosition(squares[x2][y2].getPosition().cpy());
        //squares[x2][y2].setPosition(tempV);

        squares[x1][y1].effectXY(tempV, tempV1, .2f, .0f);
        squares[x2][y2].effectXY(tempV1, tempV, .2f, .0f);

        Square temp = squares[x1][y1];
        squares[x1][y1] = squares[x2][y2];
        squares[x2][y2] = temp;

        //CHANGING COLUMN AND ROW
        squares[x2][y2].setCoord(x2, y2);
        squares[x1][y1].setCoord(x1, y1);

    }

    private void swapXandYwithCheck(final Square[][] squares, final int x1, final int y1,
                                    final int x2, final int y2) {
        Vector2 tempV = squares[x1][y1].getPosition().cpy();
        Vector2 tempV1 = squares[x2][y2].getPosition().cpy();
        //squares[x1][y1].setPosition(squares[x2][y2].getPosition().cpy());
        //squares[x2][y2].setPosition(tempV);

        squares[x1][y1].effectXY(tempV, tempV1, .2f, .0f);
        squares[x2][y2].effectXY(tempV1, tempV, .2f, .0f);

        Square temp = squares[x1][y1];
        squares[x1][y1] = squares[x2][y2];
        squares[x2][y2] = temp;

        //CHANGING COLUMN AND ROW
        squares[x2][y2].setCoord(x2, y2);
        squares[x1][y1].setCoord(x1, y1);


        Value timer = new Value();
        Tween.to(timer, -1, .21f).target(1).setCallbackTriggers(TweenCallback.COMPLETE)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        if (world.board.check().size == 0) {
                            swapXandY(squares, x2, y2, x1, y1);
                        } else {
                            world.board.control();
                        }
                    }
                }).start(getManager());


    }

    @Override
    public void effectXY(Vector2 from, Vector2 to, float duration, float delay) {
        position.y = from.y;
        Tween.to(position, VectorAccessor.VERTICAL, duration).target(to.y).delay(delay)
                .ease(TweenEquations.easeInOutSine).start(getManager());
        position.x = from.x;
        Tween.to(position, VectorAccessor.HORIZONTAL, duration).target(to.x).delay(delay)
                .ease(TweenEquations.easeInOutSine).start(getManager());
    }

    public void dissapear() {
        scale(1, 0, .3f, .1f);
        fadeOut(.3f, .1f);
        setType(-1);

    }

    public void setType(int i) {
        type = numToType(i);
    }

    public void fallingEffect(Vector2 vector2, float delay) {
        position.y = vector2.y;
        effectY(vector2.y + 300, vector2.y, .3f, delay);
        fadeIn(.3f, delay);
        scale(0, 1, .3f, delay);
    }
}
