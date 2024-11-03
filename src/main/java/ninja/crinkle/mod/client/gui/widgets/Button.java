package ninja.crinkle.mod.client.gui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.network.chat.Component;
import ninja.crinkle.mod.client.gui.events.ClickEvent;
import ninja.crinkle.mod.client.gui.events.DragEvent;
import ninja.crinkle.mod.client.gui.events.NarrateEvent;
import ninja.crinkle.mod.client.gui.events.listeners.NarrateListener;
import ninja.crinkle.mod.client.gui.properties.*;
import ninja.crinkle.mod.client.gui.renderers.ThemeGraphics;
import ninja.crinkle.mod.util.ClientUtil;

import java.util.Optional;
import java.util.function.Consumer;

public class Button extends AbstractWidget implements NarrateListener {
    private Consumer<ClickEvent> onClick;
    private Component text;

    protected Button(Builder builder) {
        super(builder);
        this.text = builder.text();
        this.onClick = builder.onClick();
    }

    public static Builder builder() {
        return new Builder();
    }

    public Component getText() {
        return text;
    }

    @Override
    public void onNarrateEvent(NarrateEvent event) {
        if (event.cancelled()) return;
        event.narrationElementOutput().add(NarratedElementType.TITLE, getText());
        event.consumed(true);
    }

    @Override
    public void renderContent(ThemeGraphics graphics, Point pMouse, Box pBox, float pPartialTick) {
        Font font = ClientUtil.getMinecraft().font;
        if (getText() != null) {
            graphics.drawCenteredString(font, getText(), (int) pBox.centerX(),
                    (int) pBox.centerY(), appearance().getForegroundColor().ABGR(),
                    appearance().hasShadow());
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        super.onClick(event);
        if (box().contains(event.position())
                && event.isLeftButton() && onClick != null) {
            event.consumed(true);
            onClick.accept(event);
        }
    }

    public void onClick(Consumer<ClickEvent> onClick) {
        this.onClick = onClick;
    }

    public void setText(Component text) {
        this.text = text;
    }

    public static class Builder extends AbstractBuilder<Builder> {
        private Component text;
        private Consumer<ClickEvent> onClick;

        @Override
        public Component name() {
            return Optional.ofNullable(text()).orElse(Component.literal("Button"));
        }

        public Component text() {
            return text;
        }

        public Builder text(String text) {
            return text(Component.literal(text));
        }

        public Builder translatableText(String text) {
            return text(Component.translatable(text));
        }

        public Builder text(Component text) {
            this.text = text;
            return self();
        }

        public Consumer<ClickEvent> onClick() {
            return onClick;
        }

        public Builder onClick(Consumer<ClickEvent> onClick) {
            this.onClick = onClick;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Button build() {
            return new Button(this);
        }
    }
}
