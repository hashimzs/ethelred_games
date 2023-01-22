package org.ethelred.games.core;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.ethelred.games.util.Util;

public class BasePlayerView<P extends GamePlayer> implements PlayerView
{
    public Game.Status getStatus() {
        return status;
    }

    private final Game.Status status;

    public String getShortCode() {
        return shortCode;
    }


    public record OtherPlayer(String name, boolean self, @JsonAnyGetter Map<String, Object> properties){}

    public record SelfView<PP extends GamePlayer>(@JsonUnwrapped Player p, @JsonUnwrapped PP gamePlayer){}
   @JsonProperty("self") private final SelfView self;

    private final String shortCode;
    @JsonProperty
    private final Set<ActionDefinition> availableActions = new TreeSet<>();

    @JsonProperty("players")
    private final List<OtherPlayer> otherPlayers = new ArrayList<>();

    @JsonProperty("log")
    private final GameLog log;
    public BasePlayerView(Player self, BaseGame<P> game)
    {
        this.self = new SelfView(self, game.gamePlayer(self));
        this.shortCode = game.shortCode();
        this.status = game.status();
        this.log = game.log;
            game.eachPlayer((player, gamePlayer) -> {
                    otherPlayers.add(new OtherPlayer(player.name(), self.same(player), Util.merge(game.gamePublicProperties(player), gamePlayer.publicProperties())));
            });
            availableActions.addAll(game.actionsFor(self));
    }

    @Override
    public Set<ActionDefinition> availableActions()
    {
        return availableActions;
    }
}
