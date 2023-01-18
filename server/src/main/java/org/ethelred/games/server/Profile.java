package org.ethelred.games.server;

import io.javalin.config.JavalinConfig;

public interface Profile
{
    int getPort();

    void configureServer(JavalinConfig javalinConfig);

    boolean runNode();
}
