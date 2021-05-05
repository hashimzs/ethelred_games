package org.ethelred.games.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class BaseGame<P extends Player> implements Game<P>
{
    private final long id;
    private volatile Status status = Status.PRESTART;
    private final Set<Long> readyPlayerIds = new HashSet<>(maxPlayers());
    private final List<P> players = new ArrayList<>(maxPlayers());
    private int currentPlayerIndex = 0;

    public BaseGame(long id)
    {
        this.id = id;
    }

    @Override
    public Status status()
    {
        return status;
    }

    @Override
    public long id()
    {
        return id;
    }

    @Override
    public void addPlayer(Player player)
    {
        if (status == Status.PRESTART && players.size() < maxPlayers())
        {
            players.add(createGamePlayer(player));
        } else
        {
            throw new IllegalStateException();
        }
    }

    protected abstract P createGamePlayer(Player p);

    protected abstract int maxPlayers();

    public void playerReady(Player player)
    {
        if (players.stream().noneMatch(p -> p.same(player)))
        {
            throw new InvalidActionException();
        }
        readyPlayerIds.add(player.id());
        if (players.stream().allMatch(v -> readyPlayerIds.contains(v.id())) &&
                players.size() >= minPlayers())
        {
            start();
            status = Status.IN_PROGRESS;
        }
    }

    protected abstract int minPlayers();

    protected abstract void start();

    public void winner(Player player)
    {
        // TODO announce winner
        status = Status.ENDED;
    }

    @Override
    public int playerCount()
    {
        return players.size();
    }

    private void validatePlayers()
    {
        if (players.isEmpty())
        {
            throw new NoPlayersException();
        }
    }

    @Override
    public P currentPlayer()
    {
        validatePlayers();
        return players.get(currentPlayerIndex);
    }

    @Override
    public P nextPlayer()
    {
        validatePlayers();
        currentPlayerIndex = (currentPlayerIndex + nextPlayerIncrement()) % playerCount();
        return currentPlayer();
    }

    public P peekNextPlayer()
    {
        validatePlayers();
        var index = (currentPlayerIndex + nextPlayerIncrement()) % playerCount();
        return players.get(index);
    }

    public void eachPlayer(Consumer<P> playerConsumer)
    {
        players.forEach(playerConsumer);
    }

    protected int nextPlayerIncrement()
    {
        return 1;
    }

    protected abstract PlayerView playerView(Player player);

    @Override
    public Map<Long, PlayerView> playerViews()
    {
        return players.stream()
                .collect(Collectors.toMap(
                        Identifiable::id,
                        this::playerView
                ));
    }
}
