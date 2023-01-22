package org.ethelred.games.bot;

import org.ethelred.games.core.Channel;
import org.ethelred.games.core.Engine;
import org.ethelred.games.core.PlayerManager;
import org.ethelred.games.core.StringAction;

import java.util.List;

public class BotAdapter implements BotApi {
    private final long playerId;
    private final Engine engine;
    private final PlayerManager playerManager;
    private Channel channel;

    public BotAdapter(long playerId, Engine engine, PlayerManager playerManager) {
        this.playerId = playerId;

        this.engine = engine;
        this.playerManager = playerManager;
    }

    @Override
    public GameResponse joinGame(String shortCode) {
        var response = engine.joinGame(playerId, shortCode);
        this.channel = response.channel();
        return adaptResponse(response.view());
    }

    @Override
    public void setName(String name) {
        playerManager.setName(playerId, name);
    }

    @Override
    public GameResponse action(String path, Action action) {
        return adaptResponse(
                engine.message(channel, adaptAction(action)).playerView()
        );
    }

    private org.ethelred.games.core.Action adaptAction(Action action) {
        return new StringAction(action.name(), action.value());
    }

    @Override
    public GameResponse poll(String path) {
        return adaptResponse(
                engine.playerView(channel)
        );
    }

    private GameResponse adaptResponse(org.ethelred.games.core.PlayerView playerView) {
        return new GameResponse(
                "", adaptPlayerView(playerView)
        );
    }

    private PlayerView adaptPlayerView(org.ethelred.games.core.PlayerView playerView) {
        return new PlayerView("TODO", playerView.availableActions()
                .stream()
                .map(this::adaptActionDefinition)
                .toList());
    }

    private BotApi.ActionDefinition adaptActionDefinition(org.ethelred.games.core.ActionDefinition actionDefinition) {
        return new ActionDefinition(actionDefinition.name(),
                List.copyOf(actionDefinition.possibleArguments()));
    }
}
