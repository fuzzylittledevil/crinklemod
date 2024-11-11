package ninja.crinkle.mod.client.gui.states.references;

import ninja.crinkle.mod.client.gui.properties.Scope;

import java.util.UUID;

public abstract class AbstractReference implements Reference {
    private final UUID id = UUID.randomUUID();
    private final Scope scope;

    public AbstractReference(Scope scope) {
        this.scope = scope;
    }

    @Override
    public String key() {
        return id.toString();
    }

    @Override
    public UUID uuid() {
        return id;
    }

    @Override
    public Scope scope() {
        return scope;
    }

}
