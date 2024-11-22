package ninja.crinkle.mod.client.gui.managers;

import ninja.crinkle.mod.client.gui.properties.Scope;
import ninja.crinkle.mod.client.gui.states.references.StateStorageRef;
import ninja.crinkle.mod.client.gui.states.storages.StateStorage;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public enum StateManager {
    GLOBAL;

    private final StateStorage storage;
    private final ConcurrentMap<StateStorageRef, StateStorage> managers = new ConcurrentHashMap<>();

    StateManager() {
        this.storage = new StateStorage(Scope.Global);
        managers.put(storage.reference(), storage);
    }

    static {

    }

    public static StateStorage global() {
        return GLOBAL.storage;
    }

    public static StateStorageRef screen() {
        StateStorage screen = new StateStorage(Scope.Screen);
        GLOBAL.managers.put(screen.reference(), screen);
        return screen.reference();
    }

    public static StateStorageRef local() {
        StateStorage local = new StateStorage(Scope.Local);
        GLOBAL.managers.put(local.reference(), local);
        return local.reference();
    }

    public static StateStorage get(StateStorageRef reference) {
        return GLOBAL.managers.get(reference);
    }
}
