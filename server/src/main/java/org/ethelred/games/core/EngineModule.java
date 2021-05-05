package org.ethelred.games.core;

import com.callicoder.snowflake.Snowflake;
import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public interface EngineModule
{
    @Binds Engine bindEngine(InMemoryEngineImpl impl);
    @Binds PlayerManager bindPlayerManager(InMemoryPlayerManagerImpl impl);

    @Provides static Snowflake snowflake() {
        return new Snowflake();
    }

    @Provides static ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
