package org.ethelred.games.util

import spock.lang.Specification

class TestMultiset extends Specification {
    def 'test empty'() {
        when:
        def ms = new Multiset()

        then:
        ms.size() == 0
        ms.isEmpty()
        !ms.iterator().hasNext()
    }

    def 'single element'() {
        when:
        def ms = new Multiset()
        ms.add("Hello")

        then:
        !ms.isEmpty()
        ms.size() == 1

        when:
        def it = ms.iterator()

        then:
        it.hasNext()
        it.next() == "Hello"
        !it.hasNext()
    }

    def 'two different'() {
        when:
        def ms = new Multiset()
        ms.add("Hello")
        ms.add("World")

        then:
        !ms.isEmpty()
        ms.size() == 2

        when:
        def it = ms.iterator()

        then:
        it.hasNext()
        it.next() == "Hello"
        it.next() == "World"
        !it.hasNext()
    }

    def 'two same'() {
        when:
        def ms = new Multiset()
        ms.add("Hello")
        ms.add("Hello")

        then:
        !ms.isEmpty()
        ms.size() == 2

        when:
        def it = ms.iterator()

        then:
        it.hasNext()
        it.next() == "Hello"
        it.next() == "Hello"
        !it.hasNext()
    }

    def more() {
        when:
        def ms = Multiset.of('a', 'b', 'b', 'c', 'b', 'a')

        then:
        ms.size() == 6
        ms.elementSet().size() == 3

        when:
        def it = ms.iterator()

        then:
        it.next() == 'a'
        it.next() == 'a'
        it.next() == 'b'
        it.next() == 'b'
        it.next() == 'b'
        it.next() == 'c'
        !it.hasNext()
    }

}
