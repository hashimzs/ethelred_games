package org.ethelred.games.server;

import dagger.Component;
import org.ethelred.games.core.Engine;
import org.ethelred.games.core.EngineModule;
import org.ethelred.games.nuo.NuoGameDefinition;

import javax.inject.Singleton;

@Singleton
@Component(modules = EngineModule.class)
public interface GameEngineFactory
{
    Engine engine();
}
