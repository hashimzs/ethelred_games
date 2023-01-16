package org.ethelred.games.nuo;

import org.jetbrains.annotations.NotNull;

/**
 * It's a deck of cards with a discard pile.
 *
 * @author eharman
 * @since 2021-04-21
 */
public interface Deck
{
    @NotNull
    Card takeCard();
    void discard(@NotNull Card card);
}
