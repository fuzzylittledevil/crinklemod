package ninja.crinkle.mod.client.gui.widgets;

import ninja.crinkle.mod.client.gui.managers.GuiManager;
import org.jetbrains.annotations.NotNull;

public class Container extends AbstractContainer {
    public Container() {
        super();
    }

    protected Container(@NotNull AbstractContainerBuilder<?> builder) {
        super(builder);
    }

    public static Builder builder(AbstractContainer parent) {
        return new Builder(parent);
    }

    public static Builder builder(GuiManager manager) {
        return new Builder(manager);
    }

    public static class Builder extends AbstractContainerBuilder<Builder> {

        public Builder(AbstractContainer container) {
            super(container);
        }

        public Builder(GuiManager manager) {
            super(manager);
        }

        @Override
        public AbstractContainer push() {
            return parent().add(this);
        }

        @Override
        public Container pushAndReturn() {
            Container container = new Container(this);
            parent().add(container);
            return container;
        }

        @Override
        public Container build() {
            return new Container(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
