package org.ethelred.games.serialization;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.ethelred.games.core.Action;

public class MyCustomSerializationModule extends SimpleModule {
    public MyCustomSerializationModule() {
        super();

        addSerializer(new MyMultisetSerializer());
        addDeserializer(Action.class, new ActionDeserializer());
    }
}
