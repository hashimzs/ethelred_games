package org.ethelred.games.nuo;

import org.ethelred.games.core.Action;
import org.ethelred.games.core.ActionPerformer;
import org.ethelred.games.core.Game;
import org.ethelred.games.core.Player;

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
    public void perform(NuoGame game, Player player, Action action)
    {
        validate(game.status() == Game.Status.IN_PROGRESS, "Game is not in progress");
        validate(game.playState() == NuoGame.PlayState.PLAY_DRAWN, "Not in expected state");
        var currentPlayer = game.currentPlayer();
        validate(player.same(currentPlayer), "Not the current player");
        var np = game.gamePlayer(player);
        validate(np.drewCard != null, "Player does not have drawn card");

        game.playState(NuoGame.PlayState.NORMAL);
        if (action.argumentAsBoolean())
        {
            // play card
            //noinspection ConstantConditions
            playCardPerformer.playCard(game, player, np.drewCard.shortCode());
        } else {
            game.nextPlayer();
        }
        np.drewCard = null;
    }
}
