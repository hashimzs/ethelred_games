package org.ethelred.games.nuo

import org.ethelred.games.core.Game
import org.ethelred.games.core.StringAction
import org.ethelred.games.util.Multiset
import spock.lang.Specification

class TestPlayCard extends Specification {
    def 'play number card'() {
        given:
            def actionPerformer = new PlayCardPerformer()
            def game = Mock(NuoGame)
            def player1 = Mock(NuoPlayer)

        when:
            actionPerformer.perform(game, new StringAction(player1, "playCard", "r6"))

        then:
            1 * game.current() >> new Card(Card.Color.RED, '4' as char)
            1 * game.status() >> Game.Status.IN_PROGRESS
            1 * game.playState() >> NuoGame.PlayState.NORMAL
            1 * game.currentPlayer() >> player1
            1 * player1.same(player1) >> true
            1 * player1.takeCard(_) >> new Card(Card.Color.RED, '6' as char)
            1 * player1.hand() >> Multiset.of(new Card(Card.Color.GREEN, '0' as char))
            1 * game.nextPlayer()
    }
}
