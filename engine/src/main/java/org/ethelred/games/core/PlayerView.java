package org.ethelred.games.core;

import java.util.Set;

public interface PlayerView {
    Set<ActionDefinition<?>> availableActions();
}
