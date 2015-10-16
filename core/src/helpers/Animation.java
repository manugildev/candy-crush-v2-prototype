package helpers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Animation {
    private Array<TextureRegion> frames;
    private float maxFrameTime;
    private float currentFrameTime;
    private float currentDelayTime;
    private int frameCount;
    private int frame;
    private float delay;
    public Sprite sprite;

    public Animation(Texture region, int frameCount, float duration, float delay) {
        frames = new Array<TextureRegion>();
        int frameWidth = region.getWidth() / frameCount;
        for (int i = 0; i < frameCount; i++) {
            frames.add(new TextureRegion(region, i * frameWidth, 0, frameWidth,
                                         region.getHeight()));
        }
        this.frameCount = frameCount;
        this.delay = delay;
        maxFrameTime = duration / frameCount;
        frame = 0;
        sprite = new Sprite();
    }

    public void update(float delta) {
        currentDelayTime += delta;
        if (currentDelayTime >= delay) {
            currentFrameTime += delta;
            if (currentFrameTime > maxFrameTime) {
                frame++;
                currentFrameTime = 0;
            }
            if (frame >= frameCount) {
                frame = 0;
                currentDelayTime = 0;
            }
        }
        sprite.setRegion(getFrame());
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public TextureRegion getFrame() {
        return frames.get(frame);
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }

    public void setSprite(float x, float y, int width, int height) {
        sprite.setPosition(x, y);
        sprite.setSize(width, height);
    }
}
