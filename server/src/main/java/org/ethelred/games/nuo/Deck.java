package org.ethelred.games.nuo;

/**
 * TODO
 *
 * @author eharman
 * @since 2021-04-21
 */
public interface Deck
{
    Card takeCard();
    void discard(Card card);
}
