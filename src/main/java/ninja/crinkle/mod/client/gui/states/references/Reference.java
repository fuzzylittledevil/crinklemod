package ninja.crinkle.mod.client.gui.states.references;

import ninja.crinkle.mod.client.gui.properties.Scope;

import java.util.UUID;

public interface Reference {
    String key();

    Scope scope();

    UUID uuid();
}
