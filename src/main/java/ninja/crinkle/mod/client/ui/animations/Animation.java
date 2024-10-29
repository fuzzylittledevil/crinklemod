package ninja.crinkle.mod.client.ui.animations;

import net.minecraft.client.gui.GuiGraphics;
import ninja.crinkle.mod.config.ClientConfig;

import java.util.*;

public class Animation {
    private double startTime;
    private double elapsedTime;
    private double frameTime;
    private double speed;
    private int frameIndex;
    private int x;
    private int y;
    private final List<CompositeFrame> frames = new ArrayList<>();

    public Animation(int x, int y, List<CompositeFrame> frames) {
        this.x = x;
        this.y = y;
        this.frames.addAll(frames);
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<CompositeFrame> getFrames() {
        return frames;
    }

    public CompositeFrame getFrame(int index) {
        return frames.get(index);
    }

    public void setSpeed(double speed) {
        this.speed = speed;
        this.frameTime = 20.0 / speed;
    }

    public double getSpeed() {
        return speed;
    }

    public void render(GuiGraphics guiGraphics, int xOffset, int yOffset, int frame) {
        getFrame(frame).render(guiGraphics, getX() + xOffset, getY() + yOffset);
    }

    public void render(GuiGraphics guiGraphics, int xOffset, int yOffset) {
        render(guiGraphics, xOffset, yOffset, getCurrentFrameIndex());
    }

    public int getCurrentFrameIndex() {
        return frameIndex;
    }

    public void update(double gameTime) {
        if (!ClientConfig.INSTANCE.overlay.metabolism.animated.get()) {
            frameIndex = 0;
            return;
        }

        if (startTime == 0) {
            startTime = gameTime;
        }
        elapsedTime = gameTime - startTime;
        frameIndex = (int) (elapsedTime / frameTime) % frames.size();
    }


    public boolean isFinished() {
        return elapsedTime >= frames.size() * frameTime;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public static class Builder {
        private double speed = 1.0;
        private int x = 0;
        private int y = 0;
        private final List<CompositeFrame> frames = new ArrayList<>();

        public Builder speed(double speed) {
            this.speed = speed;
            return this;
        }

        public Builder x(int x) {
            this.x = x;
            return this;
        }

        public Builder y(int y) {
            this.y = y;
            return this;
        }

        public Builder position(int x, int y) {
            x(x);
            return y(y);
        }

        public Builder addSpriteGroups(SpriteGroup... spriteGroups) {
            int largestIndex = Arrays.stream(spriteGroups).mapToInt(g -> g.getSprites().size()).max().orElse(0);
            if (largestIndex == 0) {
                return this;
            }
            for (int i = 0; i < largestIndex; i++) {
                List<Sprite> sprites = new ArrayList<>();
                for (SpriteGroup spriteGroup : spriteGroups) {
                    int index = i % spriteGroup.getSprites().size();
                    sprites.add(spriteGroup.getSprites().get(index));
                }
                frames.add(CompositeFrame.builder().addSprites(sprites).build());
            }
            return this;
        }

        public Animation build() {
            Animation ani = new Animation(x, y, frames);
            ani.setSpeed(speed);
            return ani;
        }
    }
}
