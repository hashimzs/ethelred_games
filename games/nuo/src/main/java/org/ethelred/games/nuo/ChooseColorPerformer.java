package org.ethelred.games.nuo;

import org.ethelred.games.core.Action;
import org.ethelred.games.core.ActionPerformer;
import org.ethelred.games.core.Game;

public class ChooseColorPerformer implements ActionPerformer<NuoGame>
{
    public static final String NAME = "chooseColor";

    @Override
    public String actionName()
    {
        return NAME;
    }

    @Override
    public void perform(NuoGame game, Action action)
    {
        validate(game.status() == Game.Status.IN_PROGRESS);
        validate(game.playState() == NuoGame.PlayState.CHOOSE_COLOR);
        validate(action.player().same(game.currentPlayer()));
        var color = Card.Color.fromCode(action.argumentAsString().charAt(0));
        game.wildColor(color);
        game.nextPlayer();
    }
}
