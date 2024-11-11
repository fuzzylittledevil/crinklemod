package ninja.crinkle.mod.client.gui.states.references;

import ninja.crinkle.mod.client.gui.managers.StateManager;
import ninja.crinkle.mod.client.gui.properties.Scope;
import ninja.crinkle.mod.client.gui.states.storages.StateStorage;

import java.util.Optional;

public class ValueRef<T> extends AbstractReference {
    private final StateStorageRef storage;
    private final Class<T> type;
    private final T defaultValue;

    public ValueRef(Scope scope, StateStorageRef storage, Class<T> type, T defaultValue) {
        super(scope);
        this.storage = storage;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    public static <T> ValueRef<T> create(Scope scope, StateStorageRef parent, Class<T> type, T defaultValue) {
        return new ValueRef<>(scope, parent, type, defaultValue);
    }

    public T defaultValue() {
        return defaultValue;
    }

    public T get() {
        return StateManager.get(storage).getValue(this).orElse(defaultValue());
    }

    public void reset() {
        StateManager.get(storage).reset(this);
    }

    public Optional<StateStorage> storage() {
        return Optional.ofNullable(StateManager.get(storage));
    }

    public Class<T> type() {
        return type;
    }

    public void set(Object value) {
        storage().ifPresent(s -> s.setValue(this, value));
    }
}
