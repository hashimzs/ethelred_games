package org.ethelred.games.core;

public record StandardChannel(long gameId, String gameType, long playerId) implements Engine.Channel
{
}
