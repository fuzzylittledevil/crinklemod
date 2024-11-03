package ninja.crinkle.mod.client.gui;

public abstract class GenericBuilder<B extends GenericBuilder<B, V>, V>  {
    public abstract V build();
    protected abstract B self();
}
