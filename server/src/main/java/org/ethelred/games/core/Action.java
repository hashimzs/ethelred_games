package org.ethelred.games.core;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.ethelred.games.core.serialization.ActionDeserializer;

@JsonDeserialize(using = ActionDeserializer.class)
public abstract class Action
{
    Player player;
    String name;

    public Action(Player player, String name)
    {
        this.player = player;
        this.name = name;
    }

    public String name()
    {
        return name;
    }
    public Player player()
    {
        return player;
    }

    public abstract String argumentAsString();
    public abstract boolean argumentAsBoolean();
}
