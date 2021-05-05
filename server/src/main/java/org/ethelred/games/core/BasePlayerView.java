package org.ethelred.games.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BasePlayerView implements Game.PlayerView
{
    Player self;
    Set<ActionDefinition> availableActions = new HashSet<>();

    public BasePlayerView(Player self, Game game)
    {
        this.self = self;
        if (game.status() == Game.Status.PRESTART)
        {
            addAction("playerReady");
        }
    }

    @Override
    public Set<ActionDefinition> availableActions()
    {
        return availableActions;
    }

    protected void addAction(String name, Object... possibleArguments)
    {
        availableActions.add(new ActionDefinition(name, possibleArguments));
    }
}
