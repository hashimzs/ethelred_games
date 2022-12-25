package org.ethelred.games.core;

public class PlayerReadyPerformer implements ActionPerformer<BaseGame<?>>
{
    @Override
    public String actionName()
    {
        return "playerReady";
    }

    @Override
    public void perform(BaseGame<?> game, Action action)
    {
        validate(game.status() == Game.Status.PRESTART);
        game.playerReady(action.player());
    }
}
