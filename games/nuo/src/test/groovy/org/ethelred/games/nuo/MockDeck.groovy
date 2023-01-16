package org.ethelred.games.nuo

class MockDeck implements Deck {
    List<Card> cards = []
    List<Card> discard = []
    List<Card> replace = []

    MockDeck(List<String> hand, String current, List<String> moreCards, int players = 2) {
        hand.each {
            cards << Card.fromCode(it)
            for (x in 1..<players) {
                cards << Card.fromCode('xw')
            }
        }
        while (cards.size() < (7 * players)) {
            cards << Card.fromCode('xw')
        }
        cards << Card.fromCode(current)
        replace = moreCards.collect {Card.fromCode(it)}
    }

    @Override
    Card takeCard() {
        if (!cards) {
            cards.addAll(replace)
        }
        cards.pop()
    }

    @Override
    void discard(Card card) {
        discard << card
    }
}
