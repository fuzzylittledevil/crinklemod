package ninja.crinkle.mod.client.gui.events;

import net.minecraft.client.gui.narration.NarrationElementOutput;
import org.jetbrains.annotations.NotNull;

public class NarrateEvent extends InputEvent {
    private final NarrationElementOutput narrationElementOutput;
    public NarrateEvent(@NotNull NarrationElementOutput pNarrationElementOutput) {
        super(EventType.Narrate);
        this.narrationElementOutput = pNarrationElementOutput;
    }

    public NarrationElementOutput narrationElementOutput() {
        return narrationElementOutput;
    }

    @Override
    public String toString() {
        return "NarrateEvent{" +
                "narrationElementOutput=" + narrationElementOutput +
                '}';
    }
}
