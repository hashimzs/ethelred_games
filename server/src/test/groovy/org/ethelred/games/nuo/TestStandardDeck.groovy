package org.ethelred.games.nuo

import spock.lang.Specification

class TestStandardDeck extends Specification {
    def 'deck has right number of cards'() {
        given:
        def random = new Random(10)
        def deck = new StandardDeck(random, { x -> })

        when:
        def counter = 0
        try {
            while (true) {
                deck.takeCard()
                counter++
            }
        } catch (ignored) {
            // ignore
        }

        then:
        counter == 108
    }

    def 'deck reshuffles from discard'() {
        given:
        def random = new Random(10)
        def counter = 0
        def deck = new StandardDeck(random, { x -> counter += 1 })

        when:
        for (i in 0..<250) {
            def card = deck.takeCard()
            deck.discard(card)
        }

        then:
        counter == 2
    }
}
