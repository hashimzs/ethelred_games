package org.ethelred.games.server;

import dagger.Component;
import org.ethelred.games.server.bind.EngineModule;
import org.ethelred.games.server.bind.NuoModule;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
        FakeIdModule.class,
        EngineModule.class,
        NuoModule.class
})
public interface TestGameEngineComponent extends GameEngineComponent {
}
