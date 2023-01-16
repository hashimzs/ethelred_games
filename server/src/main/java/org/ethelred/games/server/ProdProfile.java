package org.ethelred.games.server;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import io.javalin.config.JavalinConfig;

@Module
public class ProdProfile implements Profile
{
    @Provides
    @IntoMap
    @StringKey("production")
    public static Profile get() {
        return new ProdProfile();
    }

    @Override
    public int getPort()
    {
        return 7000;
    }

    @Override
    public void configureServer(JavalinConfig javalinConfig)
    {
        // TODO
    }
}
