package com.battledude.coincrush.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import MainGame.MainGame;

import configuration.Configuration;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = Configuration.GAME_NAME;
		config.width = (1080 / 3);
		config.height = (1920 / 3);
		new LwjglApplication(new MainGame(), config);
	}
}
