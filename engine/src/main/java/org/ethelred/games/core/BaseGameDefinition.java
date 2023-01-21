package org.ethelred.games.core;

import java.util.Set;

public abstract class BaseGameDefinition<G extends BaseGame<?>> implements GameDefinition<G> {
    @Override
    public Set<ActionPerformer<? super G>> actionPerformers() {
        return Set.of(
                new PlayerReadyPerformer(),
                new PlayAgainPerformer(),
                new AddBotPerformer()
        );
    }
}
