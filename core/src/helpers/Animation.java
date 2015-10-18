package helpers;

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
    private int times;
    private int counterTimes;
    private float delay;
    public Sprite sprite;
    private int frameCountY, frameCountX;

    public Animation(TextureRegion region, int frameCountX, int frameCountY) {
        frames = new Array<TextureRegion>();
        this.frameCountX = frameCountX;
        this.frameCountY = frameCountY;
        int frameWidth = region.getRegionWidth() / frameCountX;
        int frameHeight = region.getRegionHeight() / frameCountY;

        for (int i = 0; i < frameCountY; i++) {
            for (int j = 0; j < frameCountX; j++) {
                frames.add(new TextureRegion(region, j * frameWidth, i * frameHeight, frameWidth,
                                             frameHeight));
            }
        }

        this.frameCount = frameCountX * frameCountY;
        this.delay = 0;
        this.times = 0;
        maxFrameTime = 0;
        frame = 0;
        counterTimes = 0;
        sprite = new Sprite(getFrame());
    }

    public void update(float delta) {
        currentDelayTime += delta;
        if (counterTimes < times) {
            if (currentDelayTime >= delay) {
                currentFrameTime += delta;
                if (currentFrameTime > maxFrameTime) {
                    frame++;
                    currentFrameTime = 0;
                }
                if (frame >= frameCount) {
                    frame = 0;
                    currentDelayTime = 0;
                    counterTimes++;
                }
                sprite.setRegion(getFrame());
            }
        }
    }

    public void render(SpriteBatch batch) {
        if (currentDelayTime >= delay && counterTimes < times)
            sprite.draw(batch);

    }

    public TextureRegion getFrame() {
        return frames.get(frame);
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }

    public void setSprite(float x, float y, int width, int height) {
        sprite.setPosition(x - width / 2, y - height / 2);
        sprite.setSize(width, height);
    }

    public void start(float duration, float delay, int times) {
        this.delay = delay;
        this.times = times;
        maxFrameTime = duration / frameCount;
        frame = 0;
        counterTimes = 0;
    }

    public void changeRegion(TextureRegion region) {
        int frameWidth = region.getRegionWidth() / frameCountX;
        int frameHeight = region.getRegionHeight() / frameCountY;
        frames.clear();
        for (int i = 0; i < frameCountY; i++) {
            for (int j = 0; j < frameCountX; j++) {
                frames.add(new TextureRegion(region, j * frameWidth, i * frameHeight, frameWidth,
                                             frameHeight));
            }
        }
    }
}
