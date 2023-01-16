package org.ethelred.games.server;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

@Module
public interface FakeIdModule {
    @Singleton
    @Provides
    static Supplier<String> shortCodeSupplier() {
        return () -> "ABCD";
    }

    @Singleton @Provides static LongSupplier idSupplier() {
        AtomicLong idCounter = new AtomicLong(1000);
        return idCounter::incrementAndGet;
    }

}
