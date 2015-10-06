package helpers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Assets {

    public static AssetManager manager;

    public static void load() {
        manager = new AssetManager();
        Texture.setAssetManager(manager);

        //TEXTURE FILTER
        TextureLoader.TextureParameter param = new TextureLoader.TextureParameter();
        param.minFilter = Texture.TextureFilter.Linear;
        param.magFilter = Texture.TextureFilter.Linear;
        param.genMipMaps = true;

        // CRH Oct 2, 2015
        //
        // Had to set param null because mipmap textures are not
        // supported by robovm on ios.
        //
        param = null;

        //Textures
        manager.load("dot.png", Texture.class, param);
        manager.load("square.png", Texture.class, param);
        manager.load("background.png", Texture.class, param);
        manager.load("jewels.png", Texture.class, param);

        //Font
        manager.load("misc/font.fnt", BitmapFont.class);

        //Sounds
        manager.load("sounds/click.wav", Sound.class);
        manager.load("sounds/flap.wav", Sound.class);
        manager.load("sounds/coin.wav", Sound.class);
        manager.load("sounds/success.wav", Sound.class);
        manager.load("sounds/end.wav", Sound.class);

        //Music
        manager.load("sounds/music.mp3", Music.class);

    }

    public static void dispose() {
        manager.dispose();
    }
}
