package gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import gameworld.GameWorld;
import helpers.AssetLoader;
import helpers.FlatColors;

import static configuration.Settings.NUM_OF_SQUARES;
import static configuration.Settings.SQUARE_SIZE;


public class Board extends GameObject {

    public Square[][] squares = new Square[NUM_OF_SQUARES][NUM_OF_SQUARES];

    public Board(GameWorld world, float x, float y, float width, float height,
                 TextureRegion texture, Color color, Shape shape) {
        super(world, x, y, width, height, texture, color, shape);
        createSquares();
    }

    private void createSquares() {
        generate();
    }

    public void generate() {
        float spaceBetweenSquares = (sprite.getWidth() -
                ((NUM_OF_SQUARES) * SQUARE_SIZE)) / (NUM_OF_SQUARES + 1);
        for (int i = 0; i < NUM_OF_SQUARES; i++) {
            for (int j = 0; j < NUM_OF_SQUARES; j++) {
                float squareX = sprite
                        .getX() + ((i + 1) * spaceBetweenSquares) + (i * SQUARE_SIZE);
                float squareY = sprite.getY() + sprite.getHeight() -
                        ((j + 1) * spaceBetweenSquares) - (j * SQUARE_SIZE) - SQUARE_SIZE;
                squares[i][j] = new Square(world, squareX, squareY, SQUARE_SIZE, SQUARE_SIZE,
                        AssetLoader.square, FlatColors.WHITE, Shape.RECTANGLE, i, j,
                        MathUtils.random(1, Square.Type.values().length - 3));
            }
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        for (int i = 0; i < NUM_OF_SQUARES; i++) {
            for (int j = 0; j < NUM_OF_SQUARES; j++) {
                squares[i][j].update(delta);
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        super.render(batch, shapeRenderer);
        for (int i = 0; i < NUM_OF_SQUARES; i++) {
            for (int j = 0; j < NUM_OF_SQUARES; j++) {
                squares[i][j].render(batch, shapeRenderer);
            }
        }
    }

    public void start() {
        fadeIn(.5f, .2f);
        scale(0.8f, 1, .5f, .2f);
    }
}
