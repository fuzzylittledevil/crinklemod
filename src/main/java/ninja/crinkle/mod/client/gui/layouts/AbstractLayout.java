package ninja.crinkle.mod.client.gui.layouts;

import ninja.crinkle.mod.client.gui.builders.GenericBuilder;
import ninja.crinkle.mod.client.gui.widgets.AbstractContainer;

public abstract class AbstractLayout implements Layout {
    private Alignment alignment;
    private int spacing;

    protected AbstractLayout(AbstractBuilder<?> builder) {
        this.spacing = builder.spacing;
        this.alignment = builder.alignment;
    }

    public void alignment(Alignment alignment) {
        this.alignment = alignment;
    }

    @Override
    public Alignment alignment() {
        return alignment;
    }

    public double calculateOffset(double total, double size) {
        return switch (alignment()) {
            case CENTER -> (total - size) / 2;
            case RIGHT, BOTTOM -> total - size;
            default -> 0f;
        };
    }

    public abstract void arrange(AbstractContainer container);

    @Override
    public int spacing() {
        return spacing;
    }

    public void spacing(int spacing) {
        this.spacing = spacing;
    }

    @SuppressWarnings("unused")
    public abstract static class AbstractBuilder<T extends AbstractBuilder<T>> extends GenericBuilder<T, AbstractLayout> {
        protected Alignment alignment = Alignment.CENTER;
        protected int spacing;

        public Alignment alignment() {
            return alignment;
        }

        public T alignment(Alignment alignment) {
            this.alignment = alignment;
            return self();
        }

        @Override
        public abstract AbstractLayout build();

        public int spacing() {
            return spacing;
        }

        public T spacing(int spacing) {
            this.spacing = spacing;
            return self();
        }
    }
}
