package MainGame;

import com.badlogic.gdx.Game;

import aurelienribon.tweenengine.Tween;
import helpers.Assets;
import screens.LoadingScreen;

public class MainGame extends Game {

	public int score = 0;

	public MainGame() {
		Assets.load();
	}

	@Override
	public void create() {
		Tween.setCombinedAttributesLimit(10);
		setScreen(new LoadingScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
		Assets.manager.dispose();
		Assets.manager = null;
	}

	@Override
	public void resume() {
		if (!Assets.manager.update()) {
			setScreen(new LoadingScreen(this));
		}
	}
}