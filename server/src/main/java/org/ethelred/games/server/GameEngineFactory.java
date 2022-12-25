package org.ethelred.games.server;

import dagger.Component;
import org.ethelred.games.core.Engine;
import org.ethelred.games.server.bind.EngineModule;

import javax.inject.Singleton;

@Singleton
@Component(modules = EngineModule.class)
public interface GameEngineFactory
{
    Engine engine();
}
