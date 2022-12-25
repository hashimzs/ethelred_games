package org.ethelred.games.nuo;

import org.ethelred.games.core.BasePlayer;
import org.ethelred.games.core.Player;
import org.ethelred.games.util.Multiset;


/**
 * TODO
 *
 * @author eharman
 * @since 2021-04-21
 */
public class NuoPlayer extends BasePlayer
{
    private final Multiset<Card> hand = new Multiset<>();
//    private volatile boolean lastCard = false;
    /* package */ Card drewCard;

    public NuoPlayer(Player player)
    {
        super(player);
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

    public Multiset<Card> hand()
    {
        return hand;
    }
}
