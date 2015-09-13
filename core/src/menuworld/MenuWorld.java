package menuworld;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import MainGame.MainGame;
import gameobjects.GameObject;
import gameworld.GameWorld;
import helpers.AssetLoader;

public class MenuWorld extends GameWorld {

    public float gameWidth, gameHeight;
    public MainGame game;

    //GAMEOBJECTS

    public MenuWorld(MainGame game, float gameWidth, float gameHeight, int score) {
        super(game, gameWidth, gameHeight);

        top = new GameObject(this, 0, 0, gameWidth, gameHeight, AssetLoader.square, Color.BLACK,
                GameObject.Shape.RECTANGLE);
        top.fadeOut(.5f, 0f);
    }


    @Override
    public void update(float delta) {

        top.update(delta);
    }

    @Override
    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {

        top.render(batch, shapeRenderer);
    }

    public void goToGameScreen() {
       /* top.fadeIn(.3f, .2f);
        Value timer = new Value();
        Tween.to(timer, -1, .5f).target(0).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                MenuScreen.game.setScreen(new GameScreen(game));
            }
        }).setCallbackTriggers(TweenCallback.COMPLETE).start(title.getManager());*/

    }

}
