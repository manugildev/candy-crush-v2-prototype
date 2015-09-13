package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import MainGame.MainGame;
import gameworld.GameRenderer;
import gameworld.GameWorld;
import helpers.InputHandler;

public class GameScreen implements Screen {

    private static GameWorld world;
    public static MainGame game;
    private GameRenderer renderer;
    public float sW = Gdx.graphics.getWidth();
    public float sH = Gdx.graphics.getHeight();
    public float gameWidth = 1080;
    public float gameHeight = sH / (sW / gameWidth);

    public GameScreen(MainGame game) {
        this.game = game;
        world = new GameWorld(game, gameWidth, gameHeight);
        world.start();
        Gdx.input.setInputProcessor(new InputHandler(world, sW / gameWidth, sH / gameHeight));
        renderer = new GameRenderer(world);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        world.update(delta);
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    }
}