package ninja.crinkle.mod.client.gui.events;

import net.minecraft.client.gui.narration.NarrationElementOutput;
import ninja.crinkle.mod.client.gui.events.sources.EventSource;
import ninja.crinkle.mod.client.gui.properties.Scope;
import org.jetbrains.annotations.NotNull;

public class NarrateEvent extends AbstractEvent {
    private final NarrationElementOutput narrationElementOutput;
    public NarrateEvent(Scope scope, EventSource source, @NotNull NarrationElementOutput pNarrationElementOutput) {
        super(Type.Narrate, scope, source);
        this.narrationElementOutput = pNarrationElementOutput;
    }

    public NarrationElementOutput narrationElementOutput() {
        return narrationElementOutput;
    }

    @Override
    public String toString() {
        return "NarrateEvent{" +
                "narrationElementOutput=" + narrationElementOutput +
                ", " + super.toString() +
                '}';
    }
}
