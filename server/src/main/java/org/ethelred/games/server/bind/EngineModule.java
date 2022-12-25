package org.ethelred.games.server.bind;

import com.callicoder.snowflake.Snowflake;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import dagger.Provides;
import org.ethelred.games.core.Action;
import org.ethelred.games.core.Engine;
import org.ethelred.games.core.InMemoryEngineImpl;
import org.ethelred.games.core.InMemoryPlayerManagerImpl;
import org.ethelred.games.core.PlayerManager;
import org.ethelred.games.server.serialization.ActionDeserializer;

import javax.inject.Singleton;

@dagger.Module
public interface EngineModule
{
    @Singleton
    @Provides static
    Engine bindEngine(Snowflake idSupplier, PlayerManager playerManager) {
        return new InMemoryEngineImpl(idSupplier::nextId, playerManager);
    }
    @Singleton
    @Provides static
    PlayerManager bindPlayerManager() {
        return new InMemoryPlayerManagerImpl();
    }

    @Singleton @Provides static Snowflake snowflake() {
        return new Snowflake();
    }

    @Singleton @Provides static ObjectMapper objectMapper() {
        var r = new ObjectMapper();
        var simple = new SimpleModule();
        simple.addDeserializer(Action.class, new ActionDeserializer());
        r.registerModule(simple);
        return r;
    }
}
