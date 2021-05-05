package org.ethelred.games.server;

import io.javalin.core.JavalinConfig;

public interface Profile
{
    int getPort();

    void configureServer(JavalinConfig javalinConfig);
}
