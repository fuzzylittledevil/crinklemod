package ninja.crinkle.mod.client.gui.states.providers;

import ninja.crinkle.mod.client.gui.properties.Scope;
import ninja.crinkle.mod.client.gui.states.references.ValueRef;

public class StateValueStore<T> implements ValueStore<T> {
    private final ValueRef<T> reference;
    private T value;

    public StateValueStore(Scope scope, T defaultValue, ValueRef<T> reference) {
        this.reference = reference;
        this.value = defaultValue;
    }

    @Override
    public void reset() {
        value = reference().defaultValue();
    }

    @Override
    public ValueRef<T> reference() {
        return reference;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public void set(Object value) {
        this.value = reference().type().cast(value);
    }
}
