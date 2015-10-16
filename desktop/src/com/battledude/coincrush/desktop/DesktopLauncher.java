package com.battledude.coincrush.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import MainGame.MainGame;

import configuration.Configuration;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = Configuration.GAME_NAME;
		config.width = (int) (1080 / 2.7f);
		config.height = (int) (1920 / 2.7f);
		new LwjglApplication(new MainGame(), config);
	}
}
