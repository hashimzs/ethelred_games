package org.ethelred.games.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public abstract class BaseGame<P extends GamePlayer> implements Game
{
    private final long id;
    private String shortCode;
    private volatile Status status = Status.PRESTART;

    enum ReadyState {
        PRESTART,
        READY,
        PLAY_AGAIN,
        END
    }

    private final Map<Player, P> players = new TreeMap<>();
    private final Map<Player, ReadyState> readyPlayers = new TreeMap<>();
    private final List<Player> playerOrder = new ArrayList<>(maxPlayers());
    /* package */ final GameLog log = new GameLog(maxPlayers() * 2);

    private Player winner;
    private int currentPlayerIndex;

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
    public void shortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    @Override
    public String shortCode() {
        return shortCode;
    }

    @Override
    public void addPlayer(Player player)
    {
        if (status == Status.PRESTART && players.size() < maxPlayers()) {
            players.put(player, createGamePlayer());
            readyPlayers.put(player, ReadyState.PRESTART);
            playerOrder.add(player);
        } else if (status != Status.IN_PROGRESS || !players.containsKey(player)) {
            throw new IllegalStateException();
        }

    }

    protected abstract P createGamePlayer();

    protected abstract int maxPlayers();

    @Override
    public void playerReady(Player player)
    {
        if (!readyPlayers.containsKey(player))
        {
            throw new InvalidActionException("Player is not in this game");
        }
        readyPlayers.put(player, ReadyState.READY);
        if (readyPlayers.values().stream().allMatch(x -> x == ReadyState.READY) &&
                players.size() >= minPlayers())
        {
            start();
            status = Status.IN_PROGRESS;
        }
    }

    public void playAgain(Player player, boolean playAgain) {
        if (!readyPlayers.containsKey(player))
        {
            throw new InvalidActionException("Player is not in this game");
        }
        readyPlayers.put(player, playAgain ? ReadyState.PLAY_AGAIN : ReadyState.END);

        if (readyPlayers.values().stream().allMatch(x -> x == ReadyState.PLAY_AGAIN || x == ReadyState.END)) {
            var playAgainPlayers = readyPlayers.entrySet()
                    .stream()
                    .filter(e -> e.getValue() == ReadyState.PLAY_AGAIN)
                    .map(Map.Entry::getKey)
                    .toList();
            if (playAgainPlayers.size() >= minPlayers()) {
                throw new PlayAgainException(playAgainPlayers);
            }
        }
    }

    protected abstract int minPlayers();

    protected abstract void start();

    public void winner(Player player)
    {
        status = Status.ENDED;
        winner = player;
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
    public Player currentPlayer()
    {
        validatePlayers();
        return playerOrder.get(currentPlayerIndex);
    }

    @Override
    public Player nextPlayer()
    {
        validatePlayers();
        currentPlayerIndex = nextIndex();
        return currentPlayer();
    }

    public Player peekNextPlayer()
    {
        validatePlayers();
        var index = nextIndex();
        return playerOrder.get(index);
    }

    private int nextIndex() {
        var i = currentPlayerIndex + nextPlayerIncrement();
        if (i < 0) {
            i = playerCount() - 1;
        }
        if (i >= playerCount()) {
            i = 0;
        }
        return i;
    }

    public void eachPlayer(BiConsumer<Player, P> playerConsumer)
    {
        playerOrder.forEach(p -> playerConsumer.accept(p, players.get(p)));
    }

    /* package */Map<String,Object> gamePublicProperties(Player p) {

            var r = new HashMap<String, Object>();
            if (status == Status.PRESTART) {
                r.put("ready", readyPlayers.get(p) == ReadyState.READY);
            } else if (status == Status.IN_PROGRESS) {
                r.put("turn", p.same(currentPlayer()));
            } else if (status == Status.ENDED) {
                r.put("winner", winner != null && p.same(winner));
            }
            return Map.copyOf(r);
    }

    protected int nextPlayerIncrement()
    {
        return 1;
    }

    public abstract PlayerView playerView(Player player);

    @Override
    public Map<Player, PlayerView> playerViews()
    {
        return players.keySet().stream()
                .collect(Collectors.toMap(
                        x -> x,
                        this::playerView
                ));
    }

    public P gamePlayer(Player player) {
        return players.get(player);
    }

    public Set<ActionDefinition<?>> actionsFor(Player player) {
        if (status == Status.PRESTART && readyPlayers.get(player) == ReadyState.PRESTART) {
            return Set.of(new ActionDefinition<>("playerReady"));
        }
        if (status == Status.ENDED && readyPlayers.get(player) == ReadyState.READY) {
            return Set.of(new ActionDefinition<>("playAgain", true, false));
        }
        return Set.of();
    }

    @Override
    public void log(Player player, Action action) {
        log.push(player, action);
    }
}
