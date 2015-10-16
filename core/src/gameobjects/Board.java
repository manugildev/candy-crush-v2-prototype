package gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import configuration.Configuration;
import configuration.Settings;
import gamecontrol.Coord;
import gamecontrol.Match;
import gamecontrol.MultipleMatch;
import gamecontrol.Sols;
import gameworld.GameWorld;
import helpers.AssetLoader;
import helpers.FlatColors;
import tweens.SpriteAccessor;
import tweens.Value;

import static configuration.Settings.NUM_OF_SQUARES_X;
import static configuration.Settings.NUM_OF_SQUARES_Y;
import static configuration.Settings.NUM_OF_TYPES;
import static configuration.Settings.SPACE_BETWEEN_SQUARES;
import static configuration.Settings.SQUARE_SIZE;

public class Board extends GameObject {

    //LOGIC
    public Square[][] squares = new Square[NUM_OF_SQUARES_X][NUM_OF_SQUARES_Y];
    Vector2[][] pos = new Vector2[NUM_OF_SQUARES_X][NUM_OF_SQUARES_Y];
    private MultipleMatch matches = new MultipleMatch();
    private Match[][] columns, rows;
    private Coord[] matchCoords = new Coord[1000];
    private Sols[] solCoords = new Sols[1000];
    private Array<Sols> results = new Array<Sols>();
    float spaceBetweenSquares, diffX, diffY;
    int higherNUM;
    Array<Float> delays = new Array<Float>();
    private ArrayList<Sprite> backs = new ArrayList<Sprite>();

    //GFX
    NinePatch ninepatch;

    public Board(GameWorld world, float x, float y, float width, float height,
                 Texture texture, Color color, Shape shape) {
        super(world, x, y, width, height, texture, color, shape);

        ninepatch = new NinePatch(texture, 100, 100, 100, 100);
        delays.addAll(0.01f, 0.02f, 0.03f, 0.04f, 0.05f, 0.06f, 0.07f, 0.08f, 0.09f, 0.1f, 0.11f,
                      0.12f, 0.13f, 0.14f, 0.15f, 0.17f);
        delays.reverse();
        sprite.setAlpha(.85f);

        if (NUM_OF_SQUARES_X > NUM_OF_SQUARES_Y) higherNUM = NUM_OF_SQUARES_X;
        else higherNUM = NUM_OF_SQUARES_Y;

        columns = new Match[higherNUM][higherNUM];
        rows = new Match[higherNUM][higherNUM];
        startGame();
        start();

    }

    private void startGame() {
        for (int x = 0; x < higherNUM; ++x) {
            for (int y = 0; y < higherNUM; ++y) {
                columns[x][y] = new Match();
                rows[x][y] = new Match();

            }
        }


        for (int x = 0; x < 1000; ++x) {
            matchCoords[x] = new Coord();
            solCoords[x] = new Sols();
        }

        generate();
        fillPosArray();
    }

    public void generate() {
        boolean repeat;
        long startTime = System.currentTimeMillis();

        //GENERAL CALCULATIONS
        spaceBetweenSquares = SPACE_BETWEEN_SQUARES;
        diffX = (sprite.getWidth() - (NUM_OF_SQUARES_X * SQUARE_SIZE)
                - (spaceBetweenSquares * (NUM_OF_SQUARES_X + 1))) / 2;
        diffY = (sprite.getHeight() - (NUM_OF_SQUARES_Y * SQUARE_SIZE)
                - (spaceBetweenSquares * (NUM_OF_SQUARES_Y + 1))) / 2;

        //GENERATING NEW BOARD
        do {
            repeat = false;
            //spaceBetweenSquares = (sprite.getWidth() - ((NUM_OF_SQUARES) * SQUARE_SIZE)) / (NUM_OF_SQUARES + 1);
            for (int i = 0; i < NUM_OF_SQUARES_X; i++) {
                for (int j = 0; j < NUM_OF_SQUARES_Y; j++) {
                    squares[i][j] = createNewSquare(i, j, MathUtils.random(1, NUM_OF_TYPES - 1));
                }
            }
            if (check().size != 0) repeat = true;
            else if (solutions().size == 0) repeat = true;
            if (Settings.RANDOM_BOARD) {repeat = false; }
        } while (repeat);

        createBacks();
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Time to Generate Board --> " + elapsedTime / 1000.0);
        timercontrol(1.5f);
    }

    private void createBacks() {
        for (int i = 0; i < NUM_OF_SQUARES_X; i++) {
            for (int j = 0; j < NUM_OF_SQUARES_Y; j++) {
                Sprite backSprite = new Sprite(AssetLoader.back);
                backSprite.setAlpha(.5f);
                backSprite.setSize(Settings.SQUARE_SIZE, Settings.SQUARE_SIZE);
                backSprite
                        .setPosition(squares[i][j].getPosition().x, squares[i][j].getPosition().y);
                backSprite.setScale(1.05f);
                backSprite.setOriginCenter();
                backSprite.setAlpha(0);
                Tween.to(backSprite, SpriteAccessor.ALPHA, .5f).delay(.3f).target(.5f)
                     .start(getManager());
                //backSprite.setColor(FlatColors.WHITE);
                backs.add(backSprite);
            }
        }
    }


    @Override
    public void update(float delta) {
        super.update(delta);
        ninepatch.setColor(sprite.getColor());

        for (int i = 0; i < NUM_OF_SQUARES_X; i++) {
            for (int j = 0; j < NUM_OF_SQUARES_Y; j++) {
                squares[i][j].update(delta);
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        //super.render(batch, shapeRenderer);
        if (sprite.getScaleX() != 1) {
            float width = sprite.getWidth() * sprite.getScaleX();
            float height = sprite.getHeight() * sprite.getScaleY();
            float xn = world.gameWidth / 2 - width / 2;
            float yn = world.gameHeight / 2 - height / 2;
            ninepatch.draw(batch, xn, yn, width, height);
        } else ninepatch.draw(batch, sprite.getX(), sprite.getY(),
                              sprite.getWidth(), sprite.getHeight());

        for (int i = 0; i < backs.size(); i++) {
            backs.get(i).draw(batch);
        }
        for (int i = NUM_OF_SQUARES_X - 1; i >= 0; i--) {
            for (int j = NUM_OF_SQUARES_Y - 1; j >= 0; j--) {
                squares[i][j].render(batch, shapeRenderer);
            }
        }
    }

    public void start() {
        fadeIn(.2f, .1f);
        scale(0.8f, 1, .2f, .2f);
        for (int i = 0; i < NUM_OF_SQUARES_X; i++) {
            for (int j = 0; j < NUM_OF_SQUARES_Y; j++) {
                squares[i][j].start(((i * j) * 0.015f) + .5f);
            }
        }
    }

    public MultipleMatch check() {
        int k;
        matches.clear();
        int currCoord = 0;
        for (int y = 0; y < NUM_OF_SQUARES_Y; ++y) {
            for (int x = 0; x < NUM_OF_SQUARES_X - 2; ++x) {
                Match currentRow = rows[y][x];
                currentRow.clear();
                matchCoords[currCoord].x = x;
                matchCoords[currCoord].y = y;
                currentRow.add(matchCoords[currCoord]);
                ++currCoord;
                for (k = x + 1; k < NUM_OF_SQUARES_X; ++k) {
                    if (squares[x][y].type == squares[k][y].type && squares[x][y].type != Square.Type.EMPTY) {
                        matchCoords[currCoord].x = k;
                        matchCoords[currCoord].y = y;
                        currentRow.add(matchCoords[currCoord]);
                        ++currCoord;
                    } else {
                        break;
                    }
                }
                if (currentRow.size > 2) {
                    matches.add(currentRow);
                }
                x = k - 1;
            }
        }

        for (int x = 0; x < NUM_OF_SQUARES_X; ++x) {
            for (int y = 0; y < NUM_OF_SQUARES_Y - 2; ++y) {
                Match currentColumn = columns[x][y];
                currentColumn.clear();
                matchCoords[currCoord].x = x;
                matchCoords[currCoord].y = y;
                currentColumn.add(matchCoords[currCoord]);
                ++currCoord;

                for (k = y + 1; k < NUM_OF_SQUARES_Y; ++k) {
                    if (squares[x][y].type == squares[x][k].type &&
                            squares[x][y].type != (Square.Type.EMPTY)) {
                        matchCoords[currCoord].x = x;
                        matchCoords[currCoord].y = k;
                        currentColumn.add(matchCoords[currCoord]);
                        ++currCoord;
                    } else {
                        break;
                    }
                }
                if (currentColumn.size > 2) {
                    matches.add(currentColumn);
                }
                y = k - 1;
            }
        }

        return matches;
    }

    public Array<Sols> solutions() {
        results.clear();
        int currCoord = 0;

        if (check().size != 0) {
            solCoords[currCoord].x = -1;
            solCoords[currCoord].y = -1;
            solCoords[currCoord].d = -1;
            results.add(solCoords[currCoord]);
            ++currCoord;
            return results;
        }

        for (int x = 0; x < NUM_OF_SQUARES_X; ++x) {
            for (int y = 0; y < NUM_OF_SQUARES_Y; ++y) {

                // Swap with the one above and check
                if (y > 0) {
                    swap(x, y, x, y - 1);
                    if (check().size != 0) {
                        solCoords[currCoord].x = x;
                        solCoords[currCoord].y = y;
                        solCoords[currCoord].d = 0;
                        results.add(solCoords[currCoord]);
                        ++currCoord;
                    }

                    swap(x, y, x, y - 1);
                }

                // Swap with the one below and check
                if (y < NUM_OF_SQUARES_Y - 1) {
                    swap(x, y, x, y + 1);
                    if (check().size != 0) {
                        solCoords[currCoord].x = x;
                        solCoords[currCoord].y = y;
                        solCoords[currCoord].d = 2;
                        results.add(solCoords[currCoord]);
                        ++currCoord;
                    }

                    swap(x, y, x, y + 1);
                }

                // Swap with the one on the left and check
                if (x > 0) {
                    swap(x, y, x - 1, y);
                    if (check().size != 0) {
                        solCoords[currCoord].x = x;
                        solCoords[currCoord].y = y;
                        solCoords[currCoord].d = 1;
                        results.add(solCoords[currCoord]);
                        ++currCoord;
                    }

                    swap(x, y, x - 1, y);
                }

                // Swap with the one on the right and check
                if (x < NUM_OF_SQUARES_X - 1) {
                    swap(x, y, x + 1, y);
                    if (check().size != 0) {
                        solCoords[currCoord].x = x;
                        solCoords[currCoord].y = y;
                        solCoords[currCoord].d = 3;
                        results.add(solCoords[currCoord]);
                        ++currCoord;
                    }

                    swap(x, y, x + 1, y);
                }
            }
        }
        return results;
    }

    public void swap(int x1, int y1, int x2, int y2) {
        Square temp = squares[x1][y1];
        squares[x1][y1] = squares[x2][y2];
        squares[x2][y2] = temp;

        squares[x2][y2].setCoord(x2, y2);
        squares[x1][y1].setCoord(x1, y1);
    }

    public void swapWithoutCoords(int x1, int y1, int x2, int y2) {
        Square temp = squares[x1][y1];
        squares[x1][y1] = squares[x2][y2];
        squares[x2][y2] = temp;
    }

    private void swapCoords(int x1, int y1, int x2, int y2) {
        squares[x1][y1].setCoord(x2, y2);
        squares[x2][y2].setCoord(x1, y1);
    }

    public void control() {
        world.boardBlocked = true;
        if (check().size != 0) {
            MultipleMatch matches = check();
            for (int i = 0; i < matches.size; i++) {
                Match match = matches.get(i);
                if (matchHasBonus(match)) {
                    dissapearMatchWithBonus(match);
                } else {
                    for (int j = 0; j < match.size; j++) {
                        Coord c = match.get(j);
                        squares[c.x][c.y].dissapear();
                    }
                }
            }
            timerToControl();
        } else {
            if (solutions().size == 0) {
                destroyAll();
            } else {
                world.boardBlocked = false;
                if (Configuration.AUTOSOLVE) autoSolve();
            }


        }
    }

    public void dissapearOneLine(int x, int y, boolean isHorizontal) {
        if (isHorizontal) {
            for (int w = 0; w < NUM_OF_SQUARES_X; w++) {
                if (squares[w][y].bonus != Square.Bonus.NORMAL) squares[w][y].dissapear();
                else squares[w][y].dissapearWithBonus(true);
            }
        } else {
            for (int w = 0; w < NUM_OF_SQUARES_Y; w++) {
                if (squares[x][w].bonus != Square.Bonus.NORMAL) squares[x][w].dissapear();
                else squares[x][w].dissapearWithBonus(false);
            }
        }
    }

    public void dissapearBomb(int x, int y) {
        for (int w = x - 1; w <= x + 1; w++) {
            for (int l = y - 1; l <= y + 1; l++) {
                if (coordExists(w, l))
                    if (squares[w][l].bonus != Square.Bonus.NORMAL) squares[w][l].dissapear();
                    else squares[w][l].dissapearWithBonus(false);
            }
        }
    }

    public void dissapearBitcoin(int x, int y) {
        Square.Type type = squares[x][y].type;
        for (int w = 0; w < NUM_OF_SQUARES_X; w++) {
            for (int l = 0; l < NUM_OF_SQUARES_Y; l++) {
                if (squares[w][l].type == type)
                    if (squares[w][l].bonus != Square.Bonus.NORMAL) squares[w][l].dissapear();
                    else squares[w][l].dissapearWithBonus(false);
            }
        }
    }


    private void dissapearMatchWithBonus(Match match) {
        for (int j = 0; j < match.size; j++) {
            Coord c = match.get(j);
            switch (squares[c.x][c.y].bonus) {
                case BITCOIN:
                    dissapearBitcoin(c.x, c.y);
                    break;
                case RAY:
                    dissapearOneLine(c.x, c.y, match.isHorizontal());
                    break;
                case BOMB:
                    dissapearBomb(c.x, c.y);
                    break;
                default:
                    squares[c.x][c.y].dissapear();
                    break;
            }
        }

    }

    private boolean coordExists(int w, int l) {
        if (w >= 0 && w < NUM_OF_SQUARES_X && l >= 0 && l < NUM_OF_SQUARES_Y) return true;
        else return false;
    }

    private boolean matchHasBonus(Match match) {
        for (int j = 0; j < match.size; j++) {
            Coord c = match.get(j);
            if (squares[c.x][c.y].bonus != Square.Bonus.NORMAL) {
                return true;
            }
        }
        return false;
    }

    private void timercontrol(float delay) {
        world.boardBlocked = true;
        Value timer = new Value();
        Tween.to(timer, -1, delay).setCallbackTriggers(TweenCallback.COMPLETE).setCallback(
                new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        control();
                    }
                }).start(getManager());
    }


    private void timerToControl() {
        world.boardBlocked = true;
        Value timer = new Value();
        Tween.to(timer, -1, .1f).setCallbackTriggers(TweenCallback.COMPLETE).setCallback(
                new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        controlBucle();
                    }
                }).start(getManager());
    }

    public void controlBucle() {
        world.board.calculateEmptyBelow();
        world.board.calculateEmptyAbove();
        world.board.calculateDiffBelow();
        world.board.fall();
        world.board.refreshCR();
    }

    public void calculateEmptyBelow() {
        for (int i = 0; i < NUM_OF_SQUARES_X; i++) {
            for (int j = 0; j < NUM_OF_SQUARES_Y; j++) {
                Square cSquare = squares[i][j];
                cSquare.emptyB = 0;
                if (cSquare.type != Square.Type.EMPTY)
                    for (int l = j + 1; l < NUM_OF_SQUARES_Y; l++) {
                        if (squares[i][l].type == Square.Type.EMPTY) {
                            cSquare.emptyB++;
                        }
                    }
            }
        }
    }

    public void calculateEmptyAbove() {
        for (int i = NUM_OF_SQUARES_X - 1; i >= 0; i--) {
            for (int j = NUM_OF_SQUARES_Y - 1; j >= 1; j--) {
                Square cSquare = squares[i][j];
                if (cSquare.type == Square.Type.EMPTY) {
                    cSquare.emptyB = 0;
                    for (int l = j - 1; l >= 0; l--) {
                        if (squares[i][l].type != Square.Type.EMPTY) {
                            cSquare.emptyB--;
                        }
                    }
                }
            }
        }
    }

    public void calculateDiffBelow() {
        for (int i = 0; i < NUM_OF_SQUARES_X; i++) {
            for (int j = 0; j < NUM_OF_SQUARES_Y; j++) {
                Square cSquare = squares[i][j];
                cSquare.diffY = 0;
                if (cSquare.emptyB != 0) {
                    Square iSquare = squares[i][j + cSquare.emptyB];
                    cSquare.diffY = cSquare.getPosition().y - iSquare.getPosition().y;
                }
            }
        }
    }

    public void fall() {
        for (int i = 0; i < NUM_OF_SQUARES_X; i++) {
            for (int j = 0; j < NUM_OF_SQUARES_Y; j++) {
                Square cSquare = squares[i][j];
                if (cSquare.diffY != 0) {
                    cSquare.effectY(cSquare.getPosition().y,
                                    cSquare.getPosition().y - cSquare.diffY, .3f,
                                    delays.get(j) * 2.5f);
                }
            }
        }
    }

    public void refreshCR() {
        Value timer = new Value();
        Tween.to(timer, -1, .35f).setCallbackTriggers(TweenCallback.COMPLETE).setCallback(
                new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        fillPosArray();
                        for (int i = NUM_OF_SQUARES_X - 1; i >= 0; i--) {
                            for (int j = NUM_OF_SQUARES_Y - 1; j >= 0; j--) {
                                Square cSquare = squares[i][j];
                                if (cSquare.emptyB >= 0) {
                                    cSquare.row = j + cSquare.emptyB;
                                    swap(i, j, i, cSquare.row);
                                }
                            }
                        }

                        for (int i = NUM_OF_SQUARES_X - 1; i >= 0; i--) {
                            for (int j = NUM_OF_SQUARES_Y - 1; j >= 0; j--) {
                                Square cSquare = squares[i][j];
                                if (cSquare.type == Square.Type.EMPTY) {
                                    //TODO: CHANGE Values
                                    squares[i][j] = createNewSquare(i, j, MathUtils.random(1,
                                                                                           NUM_OF_TYPES - 1));
                                    //Gdx.app.log("Delay", String.valueOf(delays.get(j) * 2));
                                    squares[i][j].fallingEffect(pos[i][j], delays.get(j) * 2f);
                                }
                            }
                        }

                        world.boardBlocked = false;
                        timercontrol(.6f);
                    }

                }).start(getManager());

    }


    private Square createNewSquare(int i, int j, int type) {
        float squareX = sprite.getX()
                + ((i + 1) * spaceBetweenSquares) + (i * SQUARE_SIZE) + diffX;
        float squareY = sprite.getY() + sprite.getHeight() -
                ((j + 1) * spaceBetweenSquares) - (j * SQUARE_SIZE) - SQUARE_SIZE - diffY;
        return new Square(world, squareX, squareY, SQUARE_SIZE, SQUARE_SIZE,
                          AssetLoader.square, FlatColors.WHITE, Shape.RECTANGLE, i, j, type);
    }

    public void fillPosArray() {
        for (int i = 0; i < NUM_OF_SQUARES_X; i++) {
            for (int j = 0; j < NUM_OF_SQUARES_Y; j++) {
                float squareX = sprite
                        .getX() + ((i + 1) * spaceBetweenSquares) + (i * SQUARE_SIZE) + diffX;
                float squareY = sprite.getY() + sprite.getHeight() -
                        ((j + 1) * spaceBetweenSquares) - (j * SQUARE_SIZE) - SQUARE_SIZE - diffY;
                pos[i][j] = new Vector2(squareX, squareY);
            }
        }
    }

    public void autoSolve() {
        Value timer = new Value();
        Tween.to(timer, -1, .15f).setCallbackTriggers(TweenCallback.COMPLETE).setCallback(
                new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        Array<Sols> sols = world.board.solutions();
                        Sols c = sols.get(MathUtils.random(0, sols.size - 1));
                        switch (c.d) {
                            case 0:
                                squares[c.x][c.y].slideDown();
                                break;
                            case 1:
                                squares[c.x][c.y].slideLeft();
                                break;
                            case 2:
                                squares[c.x][c.y].slideUp();
                                break;
                            case 3:
                                squares[c.x][c.y].slideRight();
                                break;
                        }
                    }
                }).start(getManager());

    }

    public void destroyAll() {
        for (int i = 0; i < NUM_OF_SQUARES_X; i++) {
            for (int j = 0; j < NUM_OF_SQUARES_Y; j++) {
                squares[i][j].dissapear();
            }
        }
        timerToControl();
    }
}
