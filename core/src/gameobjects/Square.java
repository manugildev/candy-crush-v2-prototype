package gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
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
import tweens.SpriteAccessor;
import tweens.Value;
import tweens.VectorAccessor;
import ui.Text;

public class Square extends GameObject {


    public int column, row;
    private Text text;
    public int typeN, emptyB = 0;
    public float diffY = 0;
    private Sprite bonusSprite, dissEffect;

    private ParticleEffect particleEffect;
    public boolean isSelected = false;

    public Bonus bonus;
    public Type type;

    public enum Type {EMPTY, WHITE, RED, PURPLE, ORANGE, GREEN, YELLOW, BLUE}

    public enum Bonus {NORMAL, RAY, BOMB, BITCOIN}

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


        bonusSprite = new Sprite(AssetLoader.square);
        bonusSprite.setColor(FlatColors.WHITE);
        bonusSprite.setSize(width / 3, height / 3);
        bonusSprite.setScale(1);
        bonusSprite.setOriginCenter();

        dissEffect = new Sprite(AssetLoader.dissEffect);
        dissEffect.setColor(FlatColors.WHITE);
        dissEffect.setSize(width, height);
        dissEffect.setScale(0);
        dissEffect.setAlpha(1);
        dissEffect.setOriginCenter();

        //PARTICLES
        particleEffect = new ParticleEffect();
        particleEffect.load(Gdx.files.internal("misc/hit.p"), Gdx.files.internal(""));
        particleEffect.setPosition(
                sprite.getX() + (sprite.getWidth() / 2),
                sprite.getY() + (sprite.getHeight() / 2));
        particleEffect.start();
        particleEffect.start();

        if (Math.random() < Settings.BONUS_PROB) {
            //setBonus(MathUtils.random(1, Bonus.values().length - 1));
            setBonus(MathUtils.random(1,2));
        } else {
            setBonus(0);
        }

    }

    public Square(GameWorld world) {
        super(world, 0, 0, 0, 0, AssetLoader.square, FlatColors.WHITE, Shape.RECTANGLE);
    }

    public void update(float delta) {
        super.update(delta);
        particleEffect.update(delta);
        text.update(delta);
        text.setText(column + "" + row /*+ "\nE:" + emptyB*/);
        text.setPosition(getPosition().x + 10, getPosition().y);
        //if (isSelected)
        if (bonus != Bonus.NORMAL) {
            bonusSprite.setPosition(
                    sprite.getX() + (sprite.getWidth() / 2) - (bonusSprite.getWidth() / 2),
                    sprite.getY() + (sprite.getHeight() / 2) - (bonusSprite.getHeight() / 2));
            bonusSprite.setScale(sprite.getScaleX(), sprite.getScaleY());
            bonusSprite.setAlpha(sprite.getColor().a);


        }
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        //if ((column+row)%2==0) backSprite.draw(batch);
        if (type == Type.EMPTY) particleEffect.draw(batch);
        super.render(batch, shapeRenderer);
        if (bonus != Bonus.NORMAL) bonusSprite.draw(batch);
        if (type == Type.EMPTY) dissEffect.draw(batch);
        // if (Configuration.DEBUG)
        // if (sprite.getScaleX() == 1) text.render(batch, shapeRenderer, GameRenderer.fontShader);
    }

    public void setCoord(int column, int row) {
        this.column = column;
        this.row = row;
    }


    public void start(float v) {
        fadeIn(.5f, v);
        scale(0, 1, .5f, v);
    }

    public void select() {
        //scale(1, .9f, .1f, .0f);
        isSelected = true;
        sprite.setRegion(AssetLoader.jewelsSelected.get(typeN - 1));
    }

    public void deSelect() {
        //scale(0.9f, 1f, .1f, .0f);
        if(isSelected){
        isSelected = false;
        sprite.setRegion(AssetLoader.jewels.get(typeN - 1));}
    }

    public void slideRight() {
        if (checkValidMovement(column + 1, row)) {
            swapXandYwithCheck(world.board.squares, column, row, column + 1, row);
        }
    }


    public void slideLeft() {
        if (checkValidMovement(column - 1, row)) {
            swapXandYwithCheck(world.board.squares, column, row, column - 1, row);
        }
    }

    public void slideDown() {
        if (checkValidMovement(column, row - 1)) {
            swapXandYwithCheck(world.board.squares, column, row, column, row - 1);
        }
    }

    public void slideUp() {
        if (checkValidMovement(column, row + 1)) {
            swapXandYwithCheck(world.board.squares, column, row, column, row + 1);
        }
    }

    private boolean checkValidMovement(int futureRow, int futureColumn) {
        //Gdx.app.log(" Slide", column + " " + row + " " + futureColumn + " " + futureRow);
        if (futureRow < 0 || futureRow > Settings.NUM_OF_SQUARES_X - 1) {
            return false;
        } else if (futureColumn < 0 || futureColumn > Settings.NUM_OF_SQUARES_Y - 1) {
            return false;
        } else {
            return true;
        }
    }

    public void swapXandY(Square[][] squares, int x1, int y1, int x2, int y2) {
        Vector2 tempV = squares[x1][y1].getPosition().cpy();
        Vector2 tempV1 = squares[x2][y2].getPosition().cpy();

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
        if (type != Type.EMPTY) {
            scale(1, 0, .3f, .1f);
            fadeOut(.3f, .1f);
            setType(-1);
            dissEffect.setPosition(
                    sprite.getX() + (sprite.getWidth() / 2) - (dissEffect.getWidth() / 2),
                    sprite.getY() + (sprite.getHeight() / 2) - (dissEffect.getHeight() / 2));
            particleEffect.setPosition(
                    sprite.getX() + (sprite.getWidth() / 2),
                    sprite.getY() + (sprite.getHeight() / 2));

            particleEffect.reset();
            Tween.to(dissEffect, SpriteAccessor.SCALE, .3f).target(1).start(getManager());
            Tween.to(dissEffect, SpriteAccessor.ALPHA, .3f).delay(.1f).target(0)
                 .start(getManager());
        }
    }

    public void dissapearWithBonus(boolean isHorizontal) {

        switch (bonus) {
            case BITCOIN:
                world.board.dissapearBitcoin(column, row);
                break;
            case RAY:
                world.board.dissapearOneLine(column, row, isHorizontal);
                break;
            case BOMB:
                world.board.dissapearBomb(column, row);
                break;
            default:
                world.board.squares[column][row].dissapear();
                break;
        }
    }

    public void setType(int i) {
        type = numToType(i);
    }

    public void setBonus(int i) {
        bonus = numToBonus(i);
    }

    public void fallingEffect(Vector2 vector2, float delay) {
        position.y = vector2.y;
        effectY(vector2.y + 300, vector2.y, .3f, delay);
        fadeIn(.3f, delay);
        scale(0, 1, .3f, delay);
    }

    public Type numToType(int num) {
        FlatColors.organizeColors();
        typeN = num;
        if (num <= Settings.NUM_OF_TYPES && num > 0)
            sprite.setRegion(AssetLoader.jewels.get(num - 1));
        switch (num) {
            case 1:
                //sprite.setColor(FlatColors.colors.get(num - 1));
                return Type.BLUE;
            case 2:
                //sprite.setColor(FlatColors.colors.get(num - 1));
                return Type.RED;
            case 3:
                //sprite.setColor(FlatColors.colors.get(num - 1));
                return Type.ORANGE;
            case 4:
                //sprite.setColor(FlatColors.colors.get(num - 1));
                return Type.PURPLE;
            case 5:
                // sprite.setColor(FlatColors.colors.get(num - 1));
                return Type.GREEN;
            case 6:
                //sprite.setColor(FlatColors.colors.get(num - 1));
                return Type.YELLOW;
            case 7:
                //sprite.setColor(FlatColors.colors.get(num - 1));
                return Type.BLUE;
            default:
                return Type.EMPTY;
        }
    }

    public Bonus numToBonus(int num) {
        switch (num) {
            case 0:
                bonusSprite.setColor(FlatColors.WHITE);
                return Bonus.NORMAL;
            case 1:
                bonusSprite.setColor(FlatColors.DARK_WHITE);
                return Bonus.RAY;
            case 2:
                bonusSprite.setColor(FlatColors.EVEN_DARK_RED);
                return Bonus.BOMB;
            case 3:
                bonusSprite.setColor(FlatColors.YELLOW);
                return Bonus.BITCOIN;
            default:
                bonusSprite.setColor(FlatColors.WHITE);
                return Bonus.NORMAL;
        }
    }
}
