package org.ethelred.games.core;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.ethelred.games.serialization.ActionDeserializer;

@JsonDeserialize(using = ActionDeserializer.class)
public abstract class Action
{
    private final String name;

    public Action(String name)
    {
        this.name = name;
    }

    public String name()
    {
        return name;
    }

    public abstract String argumentAsString();
    public abstract boolean argumentAsBoolean();

    @Override
    public String toString() {
        return "Action{" + name + ", " + argumentAsString() + "}";
    }
}
