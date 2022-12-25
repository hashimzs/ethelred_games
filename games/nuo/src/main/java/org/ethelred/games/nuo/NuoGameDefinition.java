package org.ethelred.games.nuo;

import org.ethelred.games.core.ActionPerformer;
import org.ethelred.games.core.GameDefinition;
import org.ethelred.games.core.PlayerReadyPerformer;

import java.util.Random;
import java.util.Set;

public class NuoGameDefinition implements GameDefinition<NuoGame>
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
        return Set.of(
                new PlayerReadyPerformer(),
                new ChooseColorPerformer(),
                new DrawCardPerformer(),
                playCard,
                new PlayDrawnPerformer(playCard)
        );
    }

    @Override
    public NuoGame create(long id)
    {
        return new NuoGame(id, new StandardDeck(new Random(), (x) -> System.out.printf("TODO %s%n", x)));
    }
}
