package org.ethelred.games.core;

public class AddBotPerformer implements ActionPerformer<BaseGame<?>>, EngineAccess  {
    private Engine engine;

    @Override
    public String actionName() {
        return "addBot";
    }

    @Override
    public void perform(BaseGame<?> game, Player player, Action action) {
        engine.addBot(player, game.shortCode());
    }

    @Override
    public void engine(Engine engine) {
        this.engine = engine;
    }
}
