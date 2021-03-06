package gameworld;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

import MainGame.MainGame;
import aurelienribon.tweenengine.TweenManager;
import configuration.Settings;
import gameobjects.Board;
import gameobjects.GameObject;
import helpers.Animation;
import helpers.AssetLoader;
import helpers.FlatColors;
import screens.GameScreen;
import screens.LoadingScreen;
import ui.Timer;

import static configuration.Settings.BOARD_MARGIN;
import static configuration.Settings.NUM_OF_SQUARES_X;
import static configuration.Settings.NUM_OF_SQUARES_Y;

public class GameWorld {

    public MainGame game;
    public float gameWidth, gameHeight;
    public int score = 0;

    //GAMEOBJECTS
    public GameObject background, top;
    private GameState gameState;
    public TweenManager manager;
    public Board board;
    public boolean boardBlocked = false;
    public Timer timer;

    public Animation animBomb, animRay;

    public GameWorld(MainGame game, float gameWidth, float gameHeight) {
        this.game = game;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        gameState = GameState.TUTORIAL;
        animBomb = new Animation(AssetLoader.explosion.get(0), 46, 1);
        animRay = new Animation(new TextureRegion(AssetLoader.ray), 18, 1);

    }

    public void start() {
        //GAMEOBJECTS
        manager = new TweenManager();
        top = new GameObject(this, 0, 0, gameWidth, gameHeight, AssetLoader.square,
                FlatColors.WHITE, GameObject.Shape.RECTANGLE);
        background = new GameObject(this, 0, 0, gameWidth, gameHeight,
                AssetLoader.background, FlatColors.WHITE,
                GameObject.Shape.RECTANGLE);

        //GAMEOBJECTS
        createBoard();
        animRay.setSprite(gameWidth / 2, gameHeight / 2, (int) board.getSprite().getHeight() + 20,
                (int) board.getSprite().getHeight() + 20);
        animBomb.setSprite(gameWidth / 2, gameHeight / 2, 350, 350);
        top.fadeOut(.5f, .0f);

        timer = new Timer(this, board.sprite.getX()+Settings.TIMER_PAD*2, board.sprite.getY() - Settings.TIMER_PAD - Settings.TIMER_HEIGHT,
                board.getSprite().getWidth()-Settings.TIMER_PAD*4, Settings.TIMER_HEIGHT, AssetLoader.board, FlatColors.WHITE, GameObject.Shape.RECTANGLE);

    }

    public void update(float delta) {
        animBomb.update(delta);
        animRay.update(delta);
        manager.update(delta);
        board.update(delta);
        top.update(delta);
        timer.update(delta);
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        background.render(batch, shapeRenderer);
        board.render(batch, shapeRenderer);
        top.render(batch, shapeRenderer);
        animBomb.render(batch);
        animRay.render(batch);
        timer.render(batch,shapeRenderer);
    }

    public void addScore(int i) {
        score += i;
    }

    private void saveScoreLogic() {
        AssetLoader.addGamesPlayed();
        if (score > AssetLoader.getHighScore()) {
            AssetLoader.setHighScore(score);
        }
    }

    public void goToLoadingScreen() {
        game.setScreen(new LoadingScreen(game));
    }

    public void finish() {
        gameState = GameState.GAMEOVER;
        game.score = score;
        saveScoreLogic();
    }

    public boolean isGameOver() {
        return gameState == GameState.GAMEOVER;
    }

    public boolean isRunning() {
        return gameState == GameState.RUNNING;
    }

    public boolean isTutorial() {
        return gameState == GameState.TUTORIAL;
    }

    public void gotToRunning() {
        gameState = GameState.RUNNING;
    }

    public void goToGameScreen() {
        game.setScreen(new GameScreen(game));
    }

    public void goToMenuScreen() {

    }

    public void organizeData(ArrayList<Array<Float>> PATTERN) {

    }

    //CREATING STUFF
    private void createBoard() {
        float boardW = (Settings.NUM_OF_SQUARES_X * Settings.SQUARE_SIZE) + (Settings.SPACE_BETWEEN_SQUARES * (NUM_OF_SQUARES_X + 1)) + (BOARD_MARGIN * 2);
        float boardH = (Settings.NUM_OF_SQUARES_Y * Settings.SQUARE_SIZE) + (Settings.SPACE_BETWEEN_SQUARES * (NUM_OF_SQUARES_Y + 1)) + (BOARD_MARGIN * 2);
        float boardX = gameWidth / 2 - (boardW / 2);
        float boardY = gameHeight / 2 - (boardH / 2);
        board = new Board(this, boardX, boardY, boardW, boardH, AssetLoader.board,
                FlatColors.WHITE,
                GameObject.Shape.RECTANGLE);
    }
}
