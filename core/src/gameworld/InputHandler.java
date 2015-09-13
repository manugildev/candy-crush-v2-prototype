package gameworld;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import configuration.Configuration;

public class InputHandler implements InputProcessor {

    private GameWorld world;
    private float scaleFactorX;
    private float scaleFactorY;
    int activeTouch = 0;

    public InputHandler(GameWorld world, float scaleFactorX, float scaleFactorY) {
        this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;
        this.world = world;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.R) {
        } else if (keycode == Input.Keys.F) {
        } else if (keycode == Input.Keys.D) {
            if (Configuration.DEBUG) Configuration.DEBUG = false;
            else Configuration.DEBUG = true;
        } else if (keycode == Input.Keys.M) {
        } else if (keycode == Input.Keys.S) {
        } else if (keycode == Input.Keys.L) {
            world.goToGameScreen();
        } else if (keycode == Input.Keys.SPACE) {

        } else if (keycode == Input.Keys.LEFT) {
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.RIGHT) {
        } else if (keycode == Input.Keys.LEFT) {
        }
        return false;
    }


    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);
        activeTouch--;
        return false;
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private int scaleX(int screenX) {
        return (int) (screenX / scaleFactorX);
    }

    private int scaleY(int screenY) {
        return (int) (world.gameHeight - screenY / scaleFactorY);
    }

}
