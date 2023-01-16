package org.ethelred.games.server.bind;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Provides;
import org.ethelred.games.core.Engine;
import org.ethelred.games.core.InMemoryEngineImpl;
import org.ethelred.games.core.InMemoryPlayerManagerImpl;
import org.ethelred.games.core.PlayerManager;
import org.ethelred.games.serialization.MyCustomSerializationModule;

import javax.inject.Singleton;
import java.util.function.Supplier;

@dagger.Module
public interface EngineModule
{
    @Singleton
    @Provides static
    Engine bindEngine(PlayerManager playerManager, Supplier<String> shortCodeSupplier) {
        return new InMemoryEngineImpl(playerManager, shortCodeSupplier);
    }

    @Singleton
    @Provides static
    PlayerManager bindPlayerManager() {
        return new InMemoryPlayerManagerImpl();
    }

    @Singleton @Provides static ObjectMapper objectMapper() {
        var r = new ObjectMapper();
        r.registerModule(new MyCustomSerializationModule());
        return r;
    }
}
