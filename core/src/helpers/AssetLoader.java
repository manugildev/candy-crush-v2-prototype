package helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

import configuration.Configuration;
import configuration.Settings;

public class AssetLoader {

    //BASIC
    public static Texture dotT;
    public static TextureRegion square, dot;

    //ADVANCE
    public static Texture background, jewelsTexture, dissEffect, board, back, bomb;
    public static ArrayList<TextureRegion> jewels = new ArrayList<TextureRegion>();
    public static ArrayList<TextureRegion> jewelsSelected = new ArrayList<TextureRegion>();
    public static ArrayList<TextureRegion> explosion = new ArrayList<TextureRegion>();

    //SOUNDS
    public static Sound click, flap, coinS, success, end;
    public static Music music;

    //MISC
    private static Preferences prefs;
    public static BitmapFont font, font08, font23, font12;

    //BITPLAY
    //private static GameInterface gameInterface;

    public static void load() {
        square = new TextureRegion(getAssetTexture("square.png"));
        dotT = getAssetTexture("dot.png");
        dot = new TextureRegion(dotT);

        background = getAssetTexture("background.png");
        jewelsTexture = getAssetTexture("jewels.png");
        for (int i = 0; i < Settings.NUM_OF_TYPES; i++) {
            jewels.add(new TextureRegion(jewelsTexture, 400 * i, 0, 400, 400));
        }
        jewelsTexture = getAssetTexture("jewels_selected.png");
        for (int i = 0; i < Settings.NUM_OF_TYPES; i++) {
            jewelsSelected.add(new TextureRegion(jewelsTexture, 400 * i, 0, 400, 400));
        }
        bomb = getAssetTexture("explosion.png");
        for (int i = 0; i < Settings.NUM_OF_TYPES; i++) {
            explosion.add(new TextureRegion(getAssetTexture("explosion.png"), 0,
                                            bomb.getHeight() / 5 * i, bomb.getWidth(), bomb.getHeight() / 5));
        }
        dissEffect = getAssetTexture("dissEffect.png");
        back = getAssetTexture("back.png");
        board = getAssetTexture("board2.png");


        //FONTS
        //Loading Font
        Texture tfont = new Texture(Gdx.files.internal("misc/font.png"), true);
        tfont.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);

        font = new BitmapFont(Gdx.files.internal("misc/font.fnt"), new TextureRegion(tfont),
                              true);
        font.getData().setScale(1.5f, -1.5f);
        font.setColor(FlatColors.WHITE);

        font08 = new BitmapFont(Gdx.files.internal("misc/font.fnt"), new TextureRegion(tfont),
                                true);
        font08.getData().setScale(0.6f, -0.6f);
        font08.setColor(FlatColors.WHITE);

        font12 = new BitmapFont(Gdx.files.internal("misc/font.fnt"), new TextureRegion(tfont),
                                true);
        font12.getData().setScale(1.2f, -1.2f);
        font12.setColor(FlatColors.WHITE);

        font23 = new BitmapFont(Gdx.files.internal("misc/font.fnt"), new TextureRegion(tfont),
                                true);
        font23.getData().setScale(2.3f, -2.3f);
        font23.setColor(FlatColors.WHITE);

        //PREFERENCES
        prefs = Gdx.app.getPreferences(Configuration.GAME_NAME);

        if (!prefs.contains("highScore")) {
            prefs.putInteger("highScore", 0);
        }

        if (!prefs.contains("games")) {
            prefs.putInteger("games", 0);
        }

        //Sounds
        click = getAssetSound("sounds/click.wav");
        flap = getAssetSound("sounds/flap.wav");
        coinS = getAssetSound("sounds/coin.wav");
        success = getAssetSound("sounds/success.wav");
        end = getAssetSound("sounds/end.wav");
        music = Assets.manager.get("sounds/music.mp3", Music.class);
    }

    public static void dispose() {
        font.dispose();
        font08.dispose();
        font12.dispose();
        font23.dispose();
    }

    public static void setHighScore(int val) {
        prefs.putInteger("highScore", val);
        prefs.flush();
    }

    public static void setButtonsClicked(int val) {
        prefs.putInteger("buttonsClicked", val);
        prefs.flush();
    }

    public static int getHighScore() {
        return prefs.getInteger("highScore");
    }

    public static void addGamesPlayed() {
        prefs.putInteger("games", prefs.getInteger("games") + 1);
        prefs.flush();
    }

    public static int getGamesPlayed() {
        return prefs.getInteger("games");
    }

    public static void setAds(boolean removeAdsVersion) {
        prefs = Gdx.app.getPreferences(Configuration.GAME_NAME);
        prefs.putBoolean("ads", removeAdsVersion);
        prefs.flush();
    }

    public static boolean getAds() {
        Gdx.app.log("ADS", prefs.getBoolean("ads") + "");
        return prefs.getBoolean("ads", false);
    }

    public static int getCoinNumber() {
        return prefs.getInteger("bonus");
    }

    public static void addCoinNumber(int number) {
        prefs.putInteger("bonus", prefs.getInteger("bonus") + number);
        prefs.flush();
    }

    public static void prefs() {
        prefs = Gdx.app.getPreferences(Configuration.GAME_NAME);
    }

    public static void setVolume(boolean val) {
        prefs.putBoolean("volume", val);
        prefs.flush();
    }

    public static boolean getSounds() {
        return prefs.getBoolean("sound", true);
    }

    public static void setSounds(boolean val) {
        prefs.putBoolean("sound", val);
        prefs.flush();
    }

    public static boolean getVolume() {
        return prefs.getBoolean("volume");
    }

    public static Texture getAssetTexture(String fileName) {
        return Assets.manager.get(fileName, Texture.class);
    }

    public static Sound getAssetSound(String fileName) {
        return Assets.manager.get(fileName, Sound.class);
    }

   /* public static void setGameInterface(GameInterface gi) {
        gameInterface = gi;
    }

    public static void playMusic() {
        if (music != null) {
            if (gameInterface.getCallbacks().isMusicEnabled()) {
                if (!music.isPlaying()) {
                    music.play();
                    music.setLooping(true);
                }
            } else {
                music.pause();
            }
        }
    }
    */

}
