package org.ethelred.games.nuo;

import org.ethelred.games.core.Action;
import org.ethelred.games.core.ActionPerformer;
import org.ethelred.games.core.Game;
public class PlayDrawnPerformer implements ActionPerformer<NuoGame>
{
    public static final String NAME = "playDrawn";

    private final PlayCardPerformer playCardPerformer;

    public PlayDrawnPerformer(PlayCardPerformer playCardPerformer)
    {
        this.playCardPerformer = playCardPerformer;
    }

    @Override
    public String actionName()
    {
        return NAME;
    }

    @Override
    public void perform(NuoGame game, Action action)
    {
        validate(game.status() == Game.Status.IN_PROGRESS);
        validate(game.playState() == NuoGame.PlayState.PLAY_DRAWN);
        var player = game.currentPlayer();
        validate(player.same(action.player()));
        validate(player.drewCard != null);
        var card = player.takeCard(player.drewCard);
        validate(card != null);

        if (action.argumentAsBoolean())
        {
            // play card
            //noinspection ConstantConditions
            playCardPerformer.playCard(game, player, card.shortCode());
        }
        else
        {
            // don't
            game.nextPlayer();
        }
        game.playState(NuoGame.PlayState.NORMAL);
        player.drewCard = null;
    }
}
