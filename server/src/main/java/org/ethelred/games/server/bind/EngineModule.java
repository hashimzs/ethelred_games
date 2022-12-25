package org.ethelred.games.server.bind;

import com.callicoder.snowflake.Snowflake;
import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Binds;
import dagger.Provides;
import org.ethelred.games.core.Engine;
import org.ethelred.games.core.InMemoryEngineImpl;
import org.ethelred.games.core.InMemoryPlayerManagerImpl;
import org.ethelred.games.core.PlayerManager;

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

    @Provides static Snowflake snowflake() {
        return new Snowflake();
    }

    @Provides static ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
