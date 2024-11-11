package ninja.crinkle.mod.client.gui.states.providers;

import ninja.crinkle.mod.client.gui.properties.Scope;
import ninja.crinkle.mod.client.gui.states.references.ValueRef;

public interface ValueStore<T> {
    void reset();
    ValueRef<T> reference();

    T get();

    void set(Object value);

    static <T> ValueStore<T> of(Scope scope, T value, ValueRef<T> reference) {
        return new StateValueStore<>(scope, value, reference);
    }
}
