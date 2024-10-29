package ninja.crinkle.mod.client.ui.widgets.themes;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.client.ui.themes.BoxTheme;
import ninja.crinkle.mod.client.ui.themes.Theme;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ThemedSlider extends AbstractWidget {
    private static final BoxTheme.Type TRACK_TYPE = BoxTheme.Type.SLIDER_TRACK;
    private static final BoxTheme.Type THUMB_TYPE = BoxTheme.Type.SLIDER_THUMB;
    private static final int DEFAULT_THUMB_WIDTH = 6;
    private double value = 0.0D;
    private double minValue = 0.0D;
    private double maxValue = 1.0D;
    private Theme theme;
    private Consumer<ThemedSlider> onPress = widget -> {};
    private Predicate<ThemedSlider> activePredicate = widget -> true;
    private Component label;
    private int thumbWidth;
    private int thumbHeight;
    private int trackWidth;
    private int trackHeight;
    private double heightScale = 1.0D;
    private double widthScale = 1.0D;
    private boolean dragging = false;
    private double interval = 0.0D;
    private boolean showSnapLines = true;

    public ThemedSlider(int pX, int pY, int pWidth, int pHeight, Component pMessage, Theme pTheme) {
        super(pX, pY, pWidth, pHeight, pMessage);
        setTheme(pTheme);
        setLabel(pMessage);
        setTrackWidth(pWidth);
        setTrackHeight(pHeight);
        setThumbWidth(DEFAULT_THUMB_WIDTH);
        setThumbHeight(pHeight - theme.getBorderTheme(THUMB_TYPE).edgeHeight() * 2);
    }

    public static Builder builder(Theme theme) {
        return new Builder(theme);
    }

    @Override
    public boolean isActive() {
        return super.isActive() && (activePredicate == null || activePredicate.test(this));
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        BoxTheme trackTheme = theme.getBorderTheme(TRACK_TYPE);
        BoxTheme thumbTheme = theme.getBorderTheme(THUMB_TYPE);

        int trackX = 0;
        int trackY = (getHeight() - getTrackHeight()) / 2;
        int thumbX = (int) ((value - minValue) / (maxValue - minValue) * (getTrackWidth() - getThumbWidth()));
        int thumbY = (getHeight() - getThumbHeight()) / 2;
        int trackFilledWidth = thumbX + getThumbWidth() / 2;

        Color color = isActive() ? theme.getBackgroundColor() : theme.getInactiveColor();
        pGuiGraphics.setColor((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), (float) color.getAlpha());
        renderTexture(pGuiGraphics,
                trackTheme.generateTexture(getTrackWidth(), getTrackHeight(), BoxTheme.TextureType.NORMAL),
                trackX + getX(), trackY + getY(),
                0, 0, 0, getTrackWidth(), getTrackHeight(),
                getTrackWidth(), getTrackHeight());

        Color fillColor = theme.getSecondaryColor();
        pGuiGraphics.setColor((float) fillColor.getRed(), (float) fillColor.getGreen(), (float) fillColor.getBlue(), (float) fillColor.getAlpha());
        renderTexture(pGuiGraphics,
                trackTheme.generateTexture(trackFilledWidth, getTrackHeight(), BoxTheme.TextureType.NORMAL),
                trackX + getX(), trackY + getY(),
                0, 0, 0,
                trackFilledWidth, getTrackHeight(), trackFilledWidth, getTrackHeight());

        if (interval > 0 && showSnapLines) {
            for (double i = minValue; i < maxValue; i += interval) {
                int snapX = (int) ((i - minValue) / (maxValue - minValue) * getTrackWidth());
                pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
                pGuiGraphics.vLine(getX() + snapX, getY() + trackY,getY() + trackY + getTrackHeight(), Color.BLACK.color());
            }
        }

        pGuiGraphics.setColor((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), (float) color.getAlpha());
        BoxTheme.TextureType thumbTextureType = isDragging() ? BoxTheme.TextureType.INVERTED : BoxTheme.TextureType.NORMAL;
        renderTexture(pGuiGraphics, thumbTheme.generateTexture(getThumbWidth(), getThumbHeight(), thumbTextureType),
                getX() + thumbX, getY() + thumbY,
                0, 0, 0,
                getThumbWidth(), getThumbHeight(), getThumbWidth(), getThumbHeight());
        if (isThumbHovered(pMouseX, pMouseY)) {
            pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            pGuiGraphics.renderOutline(getX() + thumbX, getY() + thumbY, getThumbWidth(), getThumbHeight(), 0xFFFFFFFF);
        }
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void onRelease(double pMouseX, double pMouseY) {
        super.onRelease(pMouseX, pMouseY);
        setDragging(false);
    }

    private int getThumbX() {
        return (int) ((value - minValue) / (maxValue - minValue) * (getTrackWidth() - getThumbWidth()));
    }

    private int getThumbY() {
        return (getHeight() - getThumbHeight()) / 2;
    }

    public Theme getTheme() {
        return theme;
    }

    public double getInterval() {
        return interval;
    }

    public void setInterval(double interval) {
        this.interval = interval;
    }

    private void dragThumb(double pMouseX) {
        double thumbXPos = pMouseX - getX() - (double) getThumbWidth() / 2;
        double trackWidth = getTrackWidth() - getThumbWidth();
        double value = (thumbXPos / trackWidth) * (getMaxValue() - getMinValue()) + getMinValue();
        // snap to interval
        if (getInterval() > 0) {
            value = Math.round(value / getInterval()) * getInterval();
        }
        setValue(Math.max(getMinValue(), Math.min(getMaxValue(), value)));
    }

    @Override
    protected void onDrag(double pMouseX, double pMouseY, double pDragX, double pDragY) {
        super.onDrag(pMouseX, pMouseY, pDragX, pDragY);
        dragThumb(pMouseX);
    }

    public boolean isThumbHovered(double pMouseX, double pMouseY) {
        int thumbX = getThumbX();
        int thumbY = getThumbY();
        return pMouseX >= getX() + thumbX && pMouseX <= getX() + thumbX + getThumbWidth() &&
                pMouseY >= getY() + thumbY && pMouseY <= getY() + thumbY + getThumbHeight();
    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        super.onClick(pMouseX, pMouseY);
        if (!isThumbHovered(pMouseX, pMouseY)) {
            dragThumb(pMouseX);
        }
        setDragging(true);
        if (onPress != null) {
            onPress.accept(this);
        }
    }

    @Override
    public int getHeight() {
        return (int) (super.getHeight() * heightScale);
    }

    @Override
    public int getWidth() {
        return (int) (super.getWidth() * widthScale);
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {
        // TODO: Implement?
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    private void setTheme(Theme theme) {
        this.theme = theme;
    }

    public Consumer<ThemedSlider> getOnPress() {
        return onPress;
    }

    private void setOnPress(Consumer<ThemedSlider> onPress) {
        this.onPress = onPress;
    }

    public Predicate<ThemedSlider> getActivePredicate() {
        return activePredicate;
    }

    private void setActivePredicate(Predicate<ThemedSlider> activePredicate) {
        this.activePredicate = activePredicate;
    }

    public Component getLabel() {
        return label;
    }

    public void setLabel(Component label) {
        this.label = label;
    }

    public int getThumbWidth() {
        return (int) (thumbWidth * widthScale);
    }

    public void setThumbWidth(int thumbWidth) {
        this.thumbWidth = thumbWidth;
    }

    public int getThumbHeight() {
        return (int) (thumbHeight * heightScale);
    }

    public void setThumbHeight(int thumbHeight) {
        this.thumbHeight = thumbHeight;
    }

    public int getTrackWidth() {
        return (int) (trackWidth * widthScale);
    }

    public void setTrackWidth(int trackWidth) {
        this.trackWidth = trackWidth;
    }

    public int getTrackHeight() {
        return (int) (trackHeight * heightScale);
    }

    public void setTrackHeight(int trackHeight) {
        this.trackHeight = trackHeight;
    }

    public double getHeightScale() {
        return heightScale;
    }

    public void setHeightScale(double heightScale) {
        this.heightScale = heightScale;
    }

    public double getWidthScale() {
        return widthScale;
    }

    public void setWidthScale(double widthScale) {
        this.widthScale = widthScale;
    }

    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public void setShowSnapLines(boolean showSnapLines) {
        this.showSnapLines = showSnapLines;
    }

    public static class Builder {
        protected final Theme theme;
        protected int x;
        protected int y;
        protected int width;
        protected int height;
        protected Component label;
        protected Consumer<ThemedSlider> onPress = widget -> {
        };
        protected Predicate<ThemedSlider> activePredicate = widget -> true;
        private double minValue;
        private double maxValue;
        private double value;
        private int thumbWidth = 6;
        private int thumbHeight = 20;
        private int trackWidth = 100;
        private int trackHeight = 20;
        private double interval = 0.0D;

        public Builder(Theme theme) {
            this.theme = theme;
        }

        public Builder x(int x) {
            this.x = x;
            return this;
        }

        public Builder y(int y) {
            this.y = y;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder label(Component label) {
            this.label = label;
            return this;
        }

        public Builder bounds(int x, int y, int width, int height) {
            return this.x(x).y(y).width(width).height(height);
        }

        public Builder min(double minValue) {
            this.minValue = minValue;
            return this;
        }

        public Builder max(double maxValue) {
            this.maxValue = maxValue;
            return this;
        }

        public Builder value(double value) {
            this.value = value;
            return this;
        }

        public Builder onPress(Consumer<ThemedSlider> onPress) {
            this.onPress = onPress;
            return this;
        }

        public Builder activePredicate(Predicate<ThemedSlider> activePredicate) {
            this.activePredicate = activePredicate;
            return this;
        }

        public Builder thumbWidth(int thumbWidth) {
            this.thumbWidth = thumbWidth;
            return this;
        }

        public Builder thumbHeight(int thumbHeight) {
            this.thumbHeight = thumbHeight;
            return this;
        }

        public Builder trackWidth(int trackWidth) {
            this.trackWidth = trackWidth;
            return this;
        }

        public Builder trackHeight(int trackHeight) {
            this.trackHeight = trackHeight;
            return this;
        }

        public Builder interval(double interval) {
            this.interval = interval;
            return this;
        }

        public ThemedSlider build() {
            ThemedSlider slider = new ThemedSlider(x, y, width, height, label, theme);
            slider.setMinValue(minValue);
            slider.setMaxValue(maxValue);
            slider.setValue(value);
            slider.setOnPress(onPress);
            slider.setActivePredicate(activePredicate);
            slider.setThumbWidth(thumbWidth);
            slider.setThumbHeight(thumbHeight);
            slider.setTrackWidth(trackWidth);
            slider.setTrackHeight(trackHeight);
            slider.setInterval(interval);
            return slider;
        }
    }
}
