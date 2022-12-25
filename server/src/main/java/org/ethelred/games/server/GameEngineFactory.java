package org.ethelred.games.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Component;
import org.ethelred.games.core.Engine;
import org.ethelred.games.core.GameDefinition;
import org.ethelred.games.server.bind.EngineModule;
import org.ethelred.games.server.bind.NuoModule;

import javax.inject.Singleton;
import java.util.Set;

@Singleton
@Component(modules = {EngineModule.class, NuoModule.class})
public interface GameEngineFactory
{
    Engine engine();

    ObjectMapper mapper();

    Set<GameDefinition<?>> gameDefinitions();
}
