package org.ethelred.games.nuo;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StandardDeck implements Deck
{
    private final List<Card> deck;
    private final Random random;
    private final Consumer<String> eventEmitter;
    private final List<Card> discard;

    @Inject
    public StandardDeck(Random random, Consumer<String> eventEmitter)
    {
        deck = _fillDeck(random);
        this.random = random;
        this.eventEmitter = eventEmitter;
        discard = new ArrayList<>(108);
    }

    private List<Card> _fillDeck(Random random)
    {
        var cards = new ArrayList<Card>(108);
        Stream.of(Card.Color.values())
                .forEach(color -> {
                    if (color == Card.Color.WILD)
                    {
                        IntStream.rangeClosed(0,3).forEach(o -> {
                            cards.add(new Card(color, Card.Type.WILD.code()));
                            cards.add(new Card(color, Card.Type.DRAW_FOUR.code()));
                        });
                    }
                    else
                    {
                        IntStream.rangeClosed(0,9).forEach(n -> {
                            cards.add(new Card(color, (char) ('0' + n)));
                            if (n > 0)
                            {
                                cards.add(new Card(color, (char) ('0' + n)));
                            }
                        });
                        IntStream.rangeClosed(0,1).forEach(o -> {
                            Stream.of(Card.Type.SKIP, Card.Type.REVERSE, Card.Type.DRAW_TWO).forEach(t -> {
                                cards.add(new Card(color, t.code()));
                            });
                        });
                    }
                });
        Collections.shuffle(cards, random);
        return cards;
    }

    @Override
    public Card takeCard()
    {
        if (deck.isEmpty())
        {
            deck.addAll(discard);
            Collections.shuffle(deck, random);
            discard.clear();
            eventEmitter.accept("Re-shuffled deck.");
        }
        return deck.remove(0);
    }

    @Override
    public void discard(Card card)
    {
        discard.add(card);
    }
}
