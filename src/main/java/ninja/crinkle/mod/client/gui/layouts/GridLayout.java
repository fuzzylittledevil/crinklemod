package ninja.crinkle.mod.client.gui.layouts;

import com.mojang.logging.LogUtils;
import ninja.crinkle.mod.client.gui.properties.Point;
import ninja.crinkle.mod.client.gui.widgets.AbstractContainer;
import ninja.crinkle.mod.client.gui.widgets.AbstractWidget;
import org.slf4j.Logger;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class GridLayout extends AbstractLayout {
    private static final Logger LOGGER = LogUtils.getLogger();
    private int columns;
    private int rows;

    protected GridLayout(Builder builder) {
        super(builder);
        this.columns = builder.columns();
        this.rows = builder.rows();
    }

    private int getRowForIndex(int index) {
        return index / columns;
    }

    private int getColumnForIndex(int index) {
        return index % columns;
    }

    @Override
    public void arrange(AbstractContainer container) {

    }

    @Override
    public int totalInnerHeight(AbstractContainer abstractContainer) {
        return 0;
    }

    @Override
    public int totalInnerWidth(AbstractContainer abstractContainer) {
        return 0;
    }

    public void columns(int columns) {
        this.columns = columns;
    }

    public int columns() {
        return columns;
    }

    public int rows() {
        return rows;
    }

    public void rows(int rows) {
        this.rows = rows;
    }

    public static class Builder extends AbstractBuilder<Builder> {
        private int columns;
        private int rows;

        public Builder() {
            super();
        }

        @Override
        public GridLayout build() {
            return new GridLayout(this);
        }

        public Builder columns(int columns) {
            this.columns = columns;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        public int columns() {
            return columns;
        }

        public Builder rows(int rows) {
            this.rows = rows;
            return self();
        }

        public int rows() {
            return rows;
        }
    }
}
