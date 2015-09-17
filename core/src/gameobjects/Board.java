package gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import configuration.Configuration;
import gamecontrol.Coord;
import gamecontrol.Match;
import gamecontrol.MultipleMatch;
import gamecontrol.Sols;
import gameworld.GameWorld;
import helpers.AssetLoader;
import helpers.FlatColors;
import tweens.Value;

import static configuration.Settings.NUM_OF_SQUARES;
import static configuration.Settings.NUM_OF_TYPES;
import static configuration.Settings.SQUARE_SIZE;

public class Board extends GameObject {

    public Square[][] squares = new Square[NUM_OF_SQUARES][NUM_OF_SQUARES];
    Square[][] tempM = new Square[NUM_OF_SQUARES][NUM_OF_SQUARES];

    Vector2[][] pos = new Vector2[NUM_OF_SQUARES][NUM_OF_SQUARES];

    private MultipleMatch matches = new MultipleMatch();
    private Match[][] columns = new Match[NUM_OF_SQUARES][NUM_OF_SQUARES - 2];
    private Match[][] rows = new Match[NUM_OF_SQUARES][NUM_OF_SQUARES - 2];
    private Coord[] matchCoords = new Coord[1000];
    private Sols[] solCoords = new Sols[1000];
    private Array<Sols> results = new Array<Sols>();

    float spaceBetweenSquares;

    public Board(GameWorld world, float x, float y, float width, float height,
                 TextureRegion texture, Color color, Shape shape) {
        super(world, x, y, width, height, texture, color, shape);
        sprite.setAlpha(.5f);
        startGame();

    }

    private void startGame() {
        for (int x = 0; x < NUM_OF_SQUARES; ++x) {
            for (int y = 0; y < NUM_OF_SQUARES - 2; ++y) {
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

        //GENERATING NEW BOARD
        do {
            repeat = false;
            spaceBetweenSquares = (sprite.getWidth() - ((NUM_OF_SQUARES) * SQUARE_SIZE)) /
                    (NUM_OF_SQUARES + 1);
            for (int i = 0; i < NUM_OF_SQUARES; i++) {
                for (int j = 0; j < NUM_OF_SQUARES; j++) {
                    squares[i][j] = createNewSquare(i, j, MathUtils.random(1, NUM_OF_TYPES - 1));
                }
            }

            if (check().size != 0) repeat = true;
            else if (solutions().size == 0) repeat = true;

        } while (repeat);

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Time to Generate Board --> " + elapsedTime / 1000.0);
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
        for (int i = NUM_OF_SQUARES - 1; i >= 0; i--) {
            for (int j = NUM_OF_SQUARES - 1; j >= 0; j--) {
                squares[i][j].render(batch, shapeRenderer);
            }
        }
    }

    public void start() {
        fadeIn(.5f, .2f);
        scale(0.8f, 1, .5f, .2f);
        for (int i = 0; i < NUM_OF_SQUARES; i++) {
            for (int j = 0; j < NUM_OF_SQUARES; j++) {
                squares[i][j].start(((i * j) * 0.025f) + 1f);
            }
        }
    }

    public MultipleMatch check() {
        int k;
        matches.clear();
        int currCoord = 0;
        for (int y = 0; y < NUM_OF_SQUARES; ++y) {
            for (int x = 0; x < NUM_OF_SQUARES - 2; ++x) {
                Match currentRow = rows[y][x];
                currentRow.clear();
                matchCoords[currCoord].x = x;
                matchCoords[currCoord].y = y;
                currentRow.add(matchCoords[currCoord]);
                ++currCoord;
                for (k = x + 1; k < NUM_OF_SQUARES; ++k) {
                    if (squares[x][y].type == squares[k][y].type && squares[x][y].type != Square.Type.sqEmpty) {
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

        for (int x = 0; x < NUM_OF_SQUARES; ++x) {
            for (int y = 0; y < NUM_OF_SQUARES - 2; ++y) {
                Match currentColumn = columns[x][y];
                currentColumn.clear();
                matchCoords[currCoord].x = x;
                matchCoords[currCoord].y = y;
                currentColumn.add(matchCoords[currCoord]);
                ++currCoord;

                for (k = y + 1; k < NUM_OF_SQUARES; ++k) {
                    if (squares[x][y].type == squares[x][k].type &&
                            squares[x][y].type != (Square.Type.sqEmpty)) {
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

        for (int x = 0; x < NUM_OF_SQUARES; ++x) {
            for (int y = 0; y < NUM_OF_SQUARES; ++y) {

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
                if (y < NUM_OF_SQUARES - 1) {
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
                if (x < NUM_OF_SQUARES - 1) {
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
        if (check().size != 0) {
            MultipleMatch matches = check();
            for (int i = 0; i < matches.size; i++) {
                Match match = matches.get(i);
                for (int j = 0; j < match.size; j++) {
                    Coord c = match.get(j);
                    squares[c.x][c.y].dissapear();
                }
            }
            timerToControl();
        } else {
            if (Configuration.AUTOSOLVE) autoSolve();
        }
    }

    private void timercontrol() {
        Value timer = new Value();
        Tween.to(timer, -1, .31f).setCallbackTriggers(TweenCallback.COMPLETE).setCallback(
                new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        control();
                    }
                }).start(getManager());
    }


    private void timerToControl() {
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
        for (int i = 0; i < NUM_OF_SQUARES; i++) {
            for (int j = 0; j < NUM_OF_SQUARES; j++) {
                Square cSquare = squares[i][j];
                cSquare.emptyB = 0;
                if (cSquare.type != Square.Type.sqEmpty)
                    for (int l = j + 1; l < NUM_OF_SQUARES; l++) {
                        if (squares[i][l].type == Square.Type.sqEmpty) {
                            cSquare.emptyB++;
                        }
                    }
            }
        }
    }

    public void calculateEmptyAbove() {
        for (int i = NUM_OF_SQUARES - 1; i >= 0; i--) {
            for (int j = NUM_OF_SQUARES - 1; j >= 1; j--) {
                Square cSquare = squares[i][j];
                if (cSquare.type == Square.Type.sqEmpty) {
                    cSquare.emptyB = 0;
                    for (int l = j - 1; l >= 0; l--) {
                        if (squares[i][l].type != Square.Type.sqEmpty) {
                            cSquare.emptyB--;
                        }
                    }
                }
            }
        }
    }

    public void calculateDiffBelow() {
        for (int i = 0; i < NUM_OF_SQUARES; i++) {
            for (int j = 0; j < NUM_OF_SQUARES; j++) {
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
        for (int i = 0; i < NUM_OF_SQUARES; i++) {
            for (int j = 0; j < NUM_OF_SQUARES; j++) {
                Square cSquare = squares[i][j];
                if (cSquare.diffY != 0) {
                    cSquare.effectY(cSquare.getPosition().y,
                            cSquare.getPosition().y - cSquare.diffY, .3f, 0.1f * (1 / (j + 1)));
                }
            }
        }
    }

    public void refreshCR() {
        Value timer = new Value();
        Tween.to(timer, -1, .31f).setCallbackTriggers(TweenCallback.COMPLETE).setCallback(
                new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        fillPosArray();
                        for (int i = NUM_OF_SQUARES - 1; i >= 0; i--) {
                            for (int j = NUM_OF_SQUARES - 1; j >= 0; j--) {
                                Square cSquare = squares[i][j];
                                if (cSquare.emptyB >= 0) {
                                    cSquare.row = j + cSquare.emptyB;
                                    swap(i, j, i, cSquare.row);
                                }
                            }
                        }

                        for (int i = NUM_OF_SQUARES - 1; i >= 0; i--) {
                            for (int j = NUM_OF_SQUARES - 1; j >= 0; j--) {
                                Square cSquare = squares[i][j];
                                if (cSquare.type == Square.Type.sqEmpty) {
                                    //TODO: CHANGE Values
                                    squares[i][j] = createNewSquare(i, j,
                                            MathUtils.random(1, NUM_OF_TYPES - 1));
                                    squares[i][j].fallingEffect(pos[i][j], 0.1f * (1 / (j + 1)));
                                }
                            }
                        }
                        timercontrol();
                    }

                }).start(getManager());

    }


    private Square createNewSquare(int i, int j, int type) {
        float squareX = sprite
                .getX() + ((i + 1) * spaceBetweenSquares) + (i * SQUARE_SIZE);
        float squareY = sprite.getY() + sprite.getHeight() -
                ((j + 1) * spaceBetweenSquares) - (j * SQUARE_SIZE) - SQUARE_SIZE;
        return new Square(world, squareX, squareY, SQUARE_SIZE, SQUARE_SIZE,
                AssetLoader.square, FlatColors.WHITE, Shape.RECTANGLE, i, j, type);
    }

    public void fillPosArray() {
        for (int i = 0; i < NUM_OF_SQUARES; i++) {
            for (int j = 0; j < NUM_OF_SQUARES; j++) {
                float squareX = sprite
                        .getX() + ((i + 1) * spaceBetweenSquares) + (i * SQUARE_SIZE);
                float squareY = sprite.getY() + sprite.getHeight() -
                        ((j + 1) * spaceBetweenSquares) - (j * SQUARE_SIZE) - SQUARE_SIZE;
                pos[i][j] = new Vector2(squareX, squareY);
            }
        }
    }

    public void autoSolve() {
        Value timer = new Value();
        Tween.to(timer, -1, .3f).setCallbackTriggers(TweenCallback.COMPLETE).setCallback(
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
}
