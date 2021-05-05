package org.ethelred.games.core;

import java.util.Set;

public interface GameDefinition<G extends Game<?>>
{
    String gameType();
    Set<ActionPerformer<? super G>> actionPerformers();
    G create(long id);
}
