package org.ethelred.games.nuo;

import org.ethelred.games.core.ActionPerformer;
import org.ethelred.games.core.BaseGameDefinition;
import org.ethelred.games.util.Util;

import java.security.SecureRandom;
import java.util.Set;

public class NuoGameDefinition extends BaseGameDefinition<NuoGame>
{

    public static final String NUO = "nuo";

    public NuoGameDefinition()
    {
    }

    @Override
    public String gameType()
    {
        return NUO;
    }

    @Override
    public Set<ActionPerformer<? super NuoGame>> actionPerformers()
    {
        var playCard = new PlayCardPerformer();
        return Util.merge(
                Set.of(
                new ChooseColorPerformer(),
                new DrawCardPerformer(),
                playCard,
                new PlayDrawnPerformer(playCard)
                ),
                super.actionPerformers()
        );
    }

    @Override
    public NuoGame create(long id)
    {
        return new NuoGame(id, new StandardDeck(new SecureRandom(), x -> System.out.printf("TODO %s%n", x)));
    }
}
