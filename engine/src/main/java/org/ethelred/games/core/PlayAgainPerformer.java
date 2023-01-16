package org.ethelred.games.core;

public class PlayAgainPerformer implements ActionPerformer<BaseGame<?>> {
    @Override
    public String actionName() {
        return "playAgain";
    }

    @Override
    public void perform(BaseGame<?> game, Player player, Action action) {
        validate(game.status() == Game.Status.ENDED, "Game has not ended");
        game.playAgain(player, action.argumentAsBoolean());
    }
}
