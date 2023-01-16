package org.ethelred.games.core;

public class PlayerReadyPerformer implements ActionPerformer<BaseGame<?>>
{
    @Override
    public String actionName()
    {
        return "playerReady";
    }

    @Override
    public void perform(BaseGame<?> game, Player player, Action action)
    {
        validate(game.status() == Game.Status.PRESTART, "Game has started");
        game.playerReady(player);
    }
}
