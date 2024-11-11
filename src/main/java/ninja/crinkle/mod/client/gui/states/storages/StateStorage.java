package ninja.crinkle.mod.client.gui.states.storages;

import com.mojang.logging.LogUtils;
import ninja.crinkle.mod.client.gui.properties.Scope;
import ninja.crinkle.mod.client.gui.states.providers.ValueStore;
import ninja.crinkle.mod.client.gui.states.references.StateStorageRef;
import ninja.crinkle.mod.client.gui.states.references.ValueRef;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class StateStorage {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Scope scope;
    private final StateStorageRef reference;
    private final ConcurrentMap<ValueRef<?>, ValueStore<?>> values = new ConcurrentHashMap<>();

    public StateStorage(Scope scope) {
        this.scope = scope;
        this.reference = StateStorageRef.create(scope);
    }

    public <T> Optional<T> getValue(ValueRef<T> ref) {
        return Optional.ofNullable(values.get(ref)).map(ValueStore::get)
                .flatMap(v -> Optional.of(ref.type().cast(v)));
    }

    public void setValue(ValueRef<?> ref, Object value) {
        if (!values.containsKey(ref) || values.get(ref) == null) {
            LOGGER.warn("Attempted to set value for non-existent reference: {}", ref);
            return;
        }

        if (value == null) {
            reset(ref);
            return;
        }
        Optional.ofNullable(values.get(ref)).ifPresent(store -> store.set(value));
    }

    public void reset(ValueRef<?> ref) {
        Optional.ofNullable(values.get(ref)).ifPresent(ValueStore::reset);
    }

    public StateStorageRef reference() {
        return reference;
    }

    public Scope scope() {
        return scope;
    }


    protected <T> void putValue(ValueRef<T> ref, ValueStore<T> store) {
        values.put(ref, store);
    }

    public <T> ValueRef<T> createValue(Class<T> type, T defaultValue) {
        ValueRef<T> ref = ValueRef.create(scope, reference, type, defaultValue);
        ValueStore<T> store = ValueStore.of(scope, defaultValue, ref);
        putValue(ref, store);
        return ref;
    }

}