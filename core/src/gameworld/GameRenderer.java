package gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import configuration.Configuration;

public class GameRenderer {

    private GameWorld world;
    private final ShapeRenderer shapeRenderer;
    public static ShaderProgram fontShader, fontShaderA;
    private GameCam camera;
    private SpriteBatch batch;
    private BitmapFont font = new BitmapFont();

    /*private final String vertexShader, fragmentShader;
    private final ShaderProgram shaderProgram;*/

    public GameRenderer(GameWorld world) {
        this.world = world;
        camera = new GameCam(world);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        initFont();
        font.getData().setScale(3);
        font.setColor(Configuration.FPS_COUNTER_COLOR);

        /*
        vertexShader = Gdx.files.internal("misc/vertex.glsl").readString();
        fragmentShader = Gdx.files.internal("misc/fragment.glsl").readString();
        shaderProgram = new ShaderProgram(vertexShader, fragmentShader);*/
    }

    private void initFont() {
        fontShader = new ShaderProgram(Gdx.files.internal("misc/font.vert"),
                Gdx.files.internal("misc/font.frag"));
        if (!fontShader.isCompiled()) {
            Gdx.app.error("fontShader", "compilation failed:\n" + fontShader.getLog());
        }

        fontShaderA = new ShaderProgram(Gdx.files.internal("misc/font.vert"),
                Gdx.files.internal("misc/fontAlpha.frag"));
        if (!fontShaderA.isCompiled()) {
            Gdx.app.error("fontShader",
                    "compilation failed:\n" + fontShader.getLog());
        }
    }

    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        batch.begin();
        camera.render(batch, shapeRenderer);
        world.render(batch, shapeRenderer);
        if (Configuration.FPS_COUNTER) {
            font.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond(), 50, 50);
        }
        batch.end();


    }

}
