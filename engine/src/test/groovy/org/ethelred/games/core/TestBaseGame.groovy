package org.ethelred.games.core

import spock.lang.Specification

class TestBaseGame extends Specification {
    def 'created game has no players'() {
        when:
        def game = new SimpleGame(3L)

        then:
        game.playerCount() == 0

        when:
        game.currentPlayer()

        then:
        thrown(NoPlayersException)
    }

    def 'can add player'() {
        given:
        def game = new SimpleGame(3)
        def p = new BasePlayer(4, "test")

        when:
        game.addPlayer(p)

        then:
        game.playerCount() == 1
        p.same(game.currentPlayer())
    }

    def 'max players'() {
        given:
        def game = new SimpleGame(3)

        when:
        (1..4).each { game.addPlayer(new BasePlayer(it, "test${it}")) }

        then:
        thrown(IllegalStateException)
    }

    def 'next player'() {
        given:
        def game = new SimpleGame(3)

        when:
        (1..3).each { game.addPlayer(new BasePlayer(it, "test${it}")) }

        then:
        game.currentPlayer().same(new BasePlayer(1, "X"))

        expect:
        game.nextPlayer().same(new BasePlayer(2, "X"))
        game.nextPlayer().same(new BasePlayer(3, "X"))
        game.nextPlayer().same(new BasePlayer(1, "X"))

    }

    def 'peek next player'() {
        given:
        def game = new SimpleGame(3)

        when:
        (1..3).each { game.addPlayer(new BasePlayer(it, "test${it}")) }

        then:
        game.currentPlayer().same(new BasePlayer(1, "X"))
        game.peekNextPlayer().same(new BasePlayer(2, "X"))
        game.currentPlayer().same(new BasePlayer(1, "X"))

    }

    def 'start game when min players are ready'() {
        when:
        def game = new SimpleGame(3)

        then:
        game.status() == Game.Status.PRESTART

        when:
        game.addPlayer(new BasePlayer(1, "test1"))
        game.playerReady(new BasePlayer(1, "X"))

        then:
        game.status() == Game.Status.PRESTART

        when:
        game.addPlayer(new BasePlayer(2, "test2"))
        game.playerReady(new BasePlayer(2, "X"))

        then:
        game.status() == Game.Status.IN_PROGRESS
    }
}

class SimpleGame extends BaseGame<Object> {

    SimpleGame(long id) {
        super(id)
    }

    @Override
    protected Object createGamePlayer() {
        return new Object()
    }

    @Override
    protected int maxPlayers() {
        return 3
    }

    @Override
    protected int minPlayers() {
        return 2
    }

    @Override
    protected void start() {

    }

    @Override
    PlayerView playerView(Player player) {
        return null
    }

    @Override
    String type() {
        return "simple"
    }

    @Override
    String shortCode() {
        return "ABC"
    }

    @Override
    Map<Long, PlayerView> playerViews() {
        throw new UnsupportedOperationException()
    }
}