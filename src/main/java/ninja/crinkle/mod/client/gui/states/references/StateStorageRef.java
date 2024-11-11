package ninja.crinkle.mod.client.gui.states.references;

import ninja.crinkle.mod.client.gui.properties.Scope;

public class StateStorageRef extends AbstractReference {
    protected StateStorageRef(Scope scope) {
        super(scope);
    }

    public static StateStorageRef create(Scope scope) {
        return new StateStorageRef(scope);
    }
}
