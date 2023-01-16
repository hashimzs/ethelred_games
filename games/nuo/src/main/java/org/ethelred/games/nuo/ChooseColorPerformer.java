package org.ethelred.games.nuo;

import org.ethelred.games.core.Action;
import org.ethelred.games.core.ActionPerformer;
import org.ethelred.games.core.Game;
import org.ethelred.games.core.Player;

public class ChooseColorPerformer implements ActionPerformer<NuoGame>
{
    public static final String NAME = "chooseColor";

    @Override
    public String actionName()
    {
        return NAME;
    }

    @Override
    public void perform(NuoGame game, Player player, Action action)
    {
        validate(game.status() == Game.Status.IN_PROGRESS, "Game is not in progress");
        validate(game.playState() == NuoGame.PlayState.CHOOSE_COLOR, "Not expecting to choose color");
        validate(player.same(game.currentPlayer()), "Not player's turn");
        var color = Color.fromCode(action.argumentAsString().charAt(0));
        game.wildColor(color);
        game.playState(NuoGame.PlayState.NORMAL);
        game.nextPlayer();
        var current = game.current();
        if (current.type() == Card.Type.DRAW_FOUR) {
            game.nextPlayer();
        }
    }
}
