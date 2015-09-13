package gameworld;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

import MainGame.MainGame;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import gameobjects.GameObject;
import helpers.AssetLoader;
import helpers.FlatColors;
import screens.GameScreen;
import screens.LoadingScreen;
import tweens.Value;
import tweens.ValueAccessor;

public class GameWorld {

    public float gameWidth, gameHeight;
    public int score = 0;
    public MainGame game;

    //GAMEOBJECTS
    public GameObject background, top;
    private GameState gameState;
    public TweenManager manager;


    public GameWorld(MainGame game, float gameWidth, float gameHeight) {
        this.game = game;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        gameState = GameState.TUTORIAL;
        Tween.registerAccessor(Value.class, new ValueAccessor());
    }

    public void start() {
        //GAMEOBJECTS
        manager = new TweenManager();
        top = new GameObject(this, 0, 0, gameWidth, gameHeight, AssetLoader.square, Color.BLACK,
                GameObject.Shape.RECTANGLE);
        top.fadeOut(.8f, .0f);
        background = new GameObject(this, 0, 0, gameWidth, gameHeight,
                AssetLoader.background, FlatColors.WHITE, GameObject.Shape.RECTANGLE);

    }

    public void update(float delta) {
        manager.update(delta);
        top.update(delta);

    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        background.render(batch, shapeRenderer);
        top.render(batch, shapeRenderer);
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
}
