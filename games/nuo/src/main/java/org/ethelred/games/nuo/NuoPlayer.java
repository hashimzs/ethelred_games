package org.ethelred.games.nuo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ethelred.games.core.GamePlayer;
import org.ethelred.games.util.Multiset;

import java.util.Map;


/**
 * TODO
 *
 * @author eharman
 * @since 2021-04-21
 */
public class NuoPlayer implements GamePlayer {
    private final Multiset<Card> hand = new Multiset<>();
//    private volatile boolean lastCard = false;
    /* package */ Card drewCard;

    public NuoPlayer()
    {
    }

    public void giveCard(Card card)
    {
        hand.add(card);
        drewCard = card;
    }

    public Card takeCard(Card card) {
        if (hand.remove(card))
        {
            return card;
        }
        return null;
    }

    @JsonProperty("hand")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Multiset<Card> hand()
    {
        return hand;
    }

    @Override
    public Map<String, Object> publicProperties() {
        return hand.isEmpty() ? Map.of() : Map.of("cardCount", hand.size());
    }
}
