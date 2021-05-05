package org.ethelred.games.nuo;

import com.google.common.collect.Multiset;
import org.ethelred.games.core.BaseGame;
import org.ethelred.games.core.BasePlayerView;
import org.ethelred.games.core.Player;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@SuppressWarnings("SwitchStatementWithTooFewBranches")
public class NuoGame extends BaseGame<NuoPlayer>
{
    enum PlayState
    {
        NORMAL,
        CHOOSE_COLOR,
        PLAY_DRAWN
    }

    private final Deck deck;
    private boolean reversedDirection = false;
    private @Nullable
    Card current;
    private @Nullable
    Card.Color wildColor;
    private @Nonnull
    PlayState playState = PlayState.NORMAL;
    // TODO last card + challenge states

    @Inject
    public NuoGame(long id, Deck deck)
    {
        super(id);
        this.deck = deck;
    }

    @Override
    public String type()
    {
        return NuoGameDefinition.NUO;
    }

    protected void start()
    {
        IntStream.rangeClosed(1, 7).forEach(n -> {
            eachPlayer(p -> p.giveCard(deck.takeCard()));
        });
        current = deck.takeCard();
    }

    @Override
    protected int nextPlayerIncrement()
    {
        return reversedDirection ? -1 : 1;
    }

    Card current()
    {
        return Objects.requireNonNull(current);
    }

    public void current(Card card)
    {
        deck.discard(Objects.requireNonNull(current));
        current = card;
    }

    public void wildColor(@Nullable Card.Color color)
    {
        wildColor = color;
    }

    void reverse()
    {
        reversedDirection = !reversedDirection;
    }

    PlayState playState()
    {
        return playState;
    }

    void playState(PlayState playState)
    {
        this.playState = playState;
    }

    @CheckForNull
    Card.Color wildColor()
    {
        return wildColor;
    }

    @Override
    protected NuoPlayer createGamePlayer(Player p)
    {
        return new NuoPlayer(p);
    }

    @Override
    protected int maxPlayers()
    {
        return 10;
    }

    @Override
    protected int minPlayers()
    {
        return 2;
    }


    @Override
    protected PlayerView playerView(Player player)
    {
        return new NuoPlayerView(player, this);
    }

    Card takeCard()
    {
        return deck.takeCard();
    }

    public static class NuoPlayerView extends BasePlayerView
    {
        record OtherPlayer(long id, String name, boolean turn, int cardCount)
        {
        }

        List<OtherPlayer> players;
        boolean reversedDirection;
        Card current;
        @Nullable
        Card.Color wildColor;
        NuoPlayer nuoSelf;

        public NuoPlayerView(Player self, NuoGame game)
        {
            super(self, game);
            players = new ArrayList<>(game.playerCount());
            game.eachPlayer(np -> {
                players.add(new OtherPlayer(
                        np.id(),
                        np.name(),
                        np.same(game.currentPlayer()),
                        np.hand().size()
                ));

                if (np.same(self))
                {
                    nuoSelf = np;
                    if (np.same(game.currentPlayer()))
                    {
                        _addPlayerTurnActions(np.hand(), game.playState());
                    }
                }
            });
            reversedDirection = game.reversedDirection;
            current = game.current();
            wildColor = game.wildColor();
        }

        private void _addPlayerTurnActions(Multiset<Card> hand, PlayState playState)
        {
            switch (playState)
            {
                case NORMAL -> {
                    addAction(PlayCardPerformer.NAME, hand.elementSet().toArray());
                    addAction(DrawCardPerformer.NAME);
                }
                case CHOOSE_COLOR -> addAction(ChooseColorPerformer.NAME, Card.Color.RED, Card.Color.GREEN, Card.Color.BLUE, Card.Color.YELLOW);
                case PLAY_DRAWN -> addAction(PlayDrawnPerformer.NAME, true, false);
            }
        }
    }

}
