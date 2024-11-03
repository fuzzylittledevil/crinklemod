package ninja.crinkle.mod.client.animations;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.GuiGraphics;
import org.slf4j.Logger;

import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;

public enum AnimationController {
    INSTANCE;
    static final Logger LOGGER = LogUtils.getLogger();
    static final int MAX_ANIMATIONS = 2; // 2 animations to prevent issues with lag
    private final Queue<Animation> animationQueue = new ArrayDeque<>(MAX_ANIMATIONS);
    private final Queue<Animation> priorityQueue = new ArrayDeque<>(MAX_ANIMATIONS);
    private Animation currentAnimation;
    private Animation priorityAnimation;

    public void queueAnimation(Animation animation) {
        if (animationQueue.size() >= MAX_ANIMATIONS) {
            animationQueue.poll();
        }
        animationQueue.add(animation);
    }

    public void queuePriorityAnimation(Animation animation) {
        if (priorityQueue.size() >= MAX_ANIMATIONS) {
            priorityQueue.poll();
        }
        priorityQueue.add(animation);
        // evict the current animation if it is not a priority animation
        currentAnimation = null;
    }

    public void clearQueue() {
        animationQueue.clear();
        priorityQueue.clear();
        currentAnimation = null;
        // leave priority animation as is to let it finish
    }

    public Animation getCurrentAnimation() {
        return Optional.ofNullable(priorityAnimation).orElse(currentAnimation);
    }

    public void render(GuiGraphics guiGraphics, double gameTime) {
        if (getCurrentAnimation() == null) {
            if (!priorityQueue.isEmpty()) {
                priorityAnimation = priorityQueue.poll();
            } else if (!animationQueue.isEmpty()) {
                currentAnimation = animationQueue.poll();
            }
        }

        Animation animation = getCurrentAnimation();
        if (animation != null) {
            animation.update(gameTime);
            animation.render(guiGraphics, 0, 0);
            if (animation.isFinished()) {
                if (animation == priorityAnimation) {
                    priorityAnimation = null;
                } else {
                    currentAnimation = null;
                }
            }
        }
    }
}
