package org.ethelred.games.server.bind;

import dagger.Provides;
import dagger.multibindings.IntoSet;
import org.ethelred.games.core.GameDefinition;
import org.ethelred.games.nuo.NuoGameDefinition;

@dagger.Module
public interface NuoModule {
    @Provides @IntoSet static GameDefinition<?> provideGame() {
        return new NuoGameDefinition();
    }
}
