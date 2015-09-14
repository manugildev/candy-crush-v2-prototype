package gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import gamecontrol.Coord;
import gamecontrol.Match;
import gamecontrol.MultipleMatch;
import gameworld.GameWorld;
import helpers.AssetLoader;
import helpers.FlatColors;
import tweens.Value;

import static configuration.Settings.NUM_OF_SQUARES;
import static configuration.Settings.SQUARE_SIZE;


public class Board extends GameObject {

    public Square[][] squares = new Square[NUM_OF_SQUARES][NUM_OF_SQUARES];

    private MultipleMatch matches = new MultipleMatch();
    private Match[][] columns = new Match[NUM_OF_SQUARES][NUM_OF_SQUARES - 2];
    private Match[][] rows = new Match[NUM_OF_SQUARES][NUM_OF_SQUARES - 2];
    private Coord[] matchCoords = new Coord[1000];
    private Coord[] solCoords = new Coord[1000];
    private Array<Coord> results = new Array<Coord>();

    public Board(GameWorld world, float x, float y, float width, float height,
                 TextureRegion texture, Color color, Shape shape) {
        super(world, x, y, width, height, texture, color, shape);
        createSquares();


    }

    private void createSquares() {
        for (int x = 0; x < NUM_OF_SQUARES; ++x) {
            for (int y = 0; y < NUM_OF_SQUARES - 2; ++y) {
                columns[x][y] = new Match();
                rows[x][y] = new Match();
            }
        }
        for (int x = 0; x < 1000; ++x) {
            matchCoords[x] = new Coord();
            solCoords[x] = new Coord();
        }
        generate();
        check();
    }

    public void generate() {

        boolean repeat = false;

        do {
            repeat = false;
            System.out.println("### Generating...");

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

            if (check().size != 0) {
                System.out.println("Generated board has matches, repeating...");
                repeat = true;
            } else if (solutions().size == 0) {
                System.out.println("Generated board doesn't have solutions, repeating...");
                repeat = true;
            }
        } while (repeat);

        System.out.println("The generated board has no matches but some possible solutions.");


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
                squares[i][j].start();
            }
        }
    }

    public MultipleMatch check() {
        int k;

        matches.clear();
        int currCoord = 0;

        // First, we check each row (horizontal)
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

    public Array<Coord> solutions() {
        results.clear();
        int currCoord = 0;

        if (check().size != 0) {
            solCoords[currCoord].x = -1;
            solCoords[currCoord].y = -1;
            results.add(solCoords[currCoord]);
            ++currCoord;
            return results;
        }

	    /*
           Check all possible boards
	       (49 * 4) + (32 * 2) although there are many duplicates
	    */
        for (int x = 0; x < NUM_OF_SQUARES; ++x) {
            for (int y = 0; y < NUM_OF_SQUARES; ++y) {

                // Swap with the one above and check
                if (y > 0) {
                    swap(x, y, x, y - 1);
                    if (check().size != 0) {
                        solCoords[currCoord].x = x;
                        solCoords[currCoord].y = y;
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

    public void control() {
        if (check().size != 0) {
            MultipleMatch matches = check();
            for (int i = 0; i < matches.size; i++) {
                Match match = matches.get(i);
                for (int j = 0; j < match.size; j++) {
                    Coord c = match.get(j);
                    squares[c.x][c.y].dissapear();
                    timerToControl();
                }
            }
            controlBucle();
        }
    }

    private void timerToControl() {
        Value timer = new Value();
        Tween.to(timer, -1, .12f).setCallbackTriggers(TweenCallback.COMPLETE).setCallback(
                new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        controlBucle();
                    }
                }).start(getManager());
    }

    public void controlBucle() {
        world.board.calculateEmptyBelow();
        world.board.calculateDiffBelow();
        world.board.fall();
        world.board.refreshCR();

    }

    /*public void fall() {
        for (int i = 0; i < NUM_OF_SQUARES; i++) {
            for (int j = NUM_OF_SQUARES - 1; j > 0; j--) {
                if (squares[i][j].type == Square.Type.sqEmpty) {
                    squares[i][j].swapXandY(squares, i, j, i, j - 1);
                    break;
                }
            }
        }
    }*/

    /*public void fall() {
        for (int i = 0; i < NUM_OF_SQUARES; i++) {
            for (int j = 0; j < NUM_OF_SQUARES - 1; j++) {
                if (squares[i][j + 1].type == Square.Type.sqEmpty) {
                    if (squares[i][j].type != Square.Type.sqEmpty) {
                        squares[i][j].swapXandY(squares, i, j, i, j + 1);
                        break;
                    }
                }
            }
        }
    }*/

    public void calculateEmptyBelow() {
        for (int i = 0; i < NUM_OF_SQUARES; i++) {
            for (int j = 0; j < NUM_OF_SQUARES - 1; j++) {
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

    public void calculateDiffBelow() {
        for (int i = 0; i < NUM_OF_SQUARES; i++) {
            for (int j = 0; j < NUM_OF_SQUARES - 1; j++) {
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
            for (int j = 0; j < NUM_OF_SQUARES - 1; j++) {
                Square cSquare = squares[i][j];
                if (cSquare.diffY != 0) {
                    cSquare.effectY(cSquare.getPosition().y,
                            cSquare.getPosition().y - cSquare.diffY, .2f, 0.1f * (1 / (j + 1)));
                }
            }
        }
    }

    public void refreshCR() {
        Value timer = new Value();
        Tween.to(timer, -1, .3f).setCallbackTriggers(TweenCallback.COMPLETE).setCallback(
                new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        for (int i = NUM_OF_SQUARES - 1; i >= 0; i--) {
                            for (int j = NUM_OF_SQUARES - 1; j >= 0; j--) {
                                Square cSquare = squares[i][j];
                                if (cSquare.emptyB != 0) {
                                    swap(i, j, i, j + cSquare.emptyB);
                                    cSquare.emptyB = 0;
                                }
                            }
                        }
                        //control();
                    }
                }).start(getManager());

    }
}
