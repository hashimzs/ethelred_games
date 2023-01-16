package org.ethelred.games.server.bind;

import com.callicoder.snowflake.Snowflake;
import dagger.Module;
import dagger.Provides;
import org.ethelred.games.core.RandomShortCodeSupplier;

import javax.inject.Singleton;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

@Module
public interface IdModule {
    @Singleton
    @Provides
    static LongSupplier idSupplier() {
        var snowflake = new Snowflake();
        return snowflake::nextId;
    }

    @Singleton @Provides
    static Supplier<String> shortCodeSupplier() {
        return new RandomShortCodeSupplier();
    }
}
