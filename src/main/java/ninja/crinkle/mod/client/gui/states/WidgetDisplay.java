package ninja.crinkle.mod.client.gui.states;

import ninja.crinkle.mod.client.gui.managers.DragManager;
import ninja.crinkle.mod.client.gui.properties.Status;
import ninja.crinkle.mod.client.gui.widgets.AbstractWidget;

public record WidgetDisplay(boolean visible, float alpha, int zIndex, Status status) {
    public WidgetDisplay {
        if (alpha < 0 || alpha > 1) {
            throw new IllegalArgumentException("Alpha must be between 0 and 1");
        }
        if (zIndex < 0) {
            throw new IllegalArgumentException("Z-Index must be greater than or equal to 0");
        }
    }

    public WidgetDisplay() {
        this(true, 1, DragManager.Z_MIN, Status.active);
    }

    public WidgetDisplay(AbstractWidget.AbstractBuilder<?> builder) {
        this(builder.visible(), builder.alpha(), builder.zIndex(), builder.status());
    }

    public WidgetDisplay withVisible(boolean visible) {
        return new WidgetDisplay(visible, alpha, zIndex, status);
    }
    public WidgetDisplay withAlpha(float alpha) {
        return new WidgetDisplay(visible, alpha, zIndex, status);
    }
    public WidgetDisplay withZIndex(int zIndex) {
        assert zIndex >= DragManager.Z_MIN && zIndex <= DragManager.Z_MAX;
        return new WidgetDisplay(visible, alpha, zIndex, status);
    }
    public WidgetDisplay withStatus(Status status) {
        return new WidgetDisplay(visible, alpha, zIndex, status);
    }

    @Override
    public String toString() {
        return "WidgetDisplay{" +
                "visible=" + visible +
                ", alpha=" + alpha +
                ", zIndex=" + zIndex +
                ", status=" + status +
                '}';
    }
}
