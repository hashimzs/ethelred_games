package org.ethelred.games.nuo;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import org.ethelred.games.core.BasePlayer;
import org.ethelred.games.core.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * TODO
 *
 * @author eharman
 * @since 2021-04-21
 */
public class NuoPlayer extends BasePlayer
{
    private final Multiset<Card> hand = HashMultiset.create();
//    private volatile boolean lastCard = false;
    /* package */ @Nullable Card drewCard;

    public NuoPlayer(Player player)
    {
        super(player);
    }

    public void giveCard(Card card)
    {
        hand.add(card);
        drewCard = card;
    }

    @CheckForNull
    public Card takeCard(Card card) {
        if (hand.remove(card))
        {
            return card;
        }
        return null;
    }

    public Multiset<Card> hand()
    {
        return Multisets.unmodifiableMultiset(hand);
    }
}
