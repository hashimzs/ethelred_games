package org.ethelred.games.nuo

import spock.lang.Specification
import spock.lang.Unroll

class TestCard extends Specification {
    @Unroll
    def '#code is a valid card'(code) {
        expect:
            Card.fromCode(code) != null

        where:
        code | _
        'xw' | _
        'xx' | _
        'r9' | _
        'ys' | _
        'br' | _
    }

    @Unroll
    def '#code is not a valid card'() {
        when:
            Card.fromCode(code)

        then:
            thrown(IllegalArgumentException)

        where:
        code | _
        'x0' | _
        'xs' | _
        'r99' | _
        'y' | _
        'a6' | _
    }

    def 'quick card test'() {
        when:
            def card = Card.fromCode('r6')

        then:
            card.color() == Color.RED
            card.type() == Card.Type.NUMBER
    }
}
