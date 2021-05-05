package org.ethelred.games.nuo;

import org.ethelred.games.core.Action;
import org.ethelred.games.core.ActionPerformer;
import org.ethelred.games.core.Game;
import org.ethelred.games.core.Player;

import javax.inject.Singleton;

@Singleton
public class DrawCardPerformer implements ActionPerformer<NuoGame>
{
    public static final String NAME = "drawCard";

    @Override
    public String actionName()
    {
        return NAME;
    }

    @Override
    public void perform(NuoGame game, Action action)
    {
        validate(game.status() == Game.Status.IN_PROGRESS);
        validate(game.playState() == NuoGame.PlayState.NORMAL);
        var player = game.currentPlayer();
        validate(action.player().same(player));
        var card = game.takeCard();
        player.giveCard(card);
        if (PlayCardPerformer.isValidPlay(game, player, card))
        {
            game.playState(NuoGame.PlayState.PLAY_DRAWN);
        }
    }
}
