package org.ethelred.games.util

import com.google.common.jimfs.Jimfs
import spock.lang.AutoCleanup
import spock.lang.Specification

import java.nio.file.FileSystem

class TestUtil extends Specification {
    @AutoCleanup
    FileSystem fileSystem = Jimfs.newFileSystem()

    def 'simple get parent'() {
        given:
        var path = fileSystem.getPath("/a/b/c")

        when:
        var parent = Util.findParent(path, {it.endsWith('b')})

        then:
        parent.toString() == '/a/b'
    }

    def 'same path get parent'() {
        given:
        var path = fileSystem.getPath("/a/b/c")

        when:
        var parent = Util.findParent(path, {it.endsWith('c')})

        then:
        parent == path
    }

    def 'no match get parent'() {
        given:
        var path = fileSystem.getPath("/a/b/c")

        when:
        var parent = Util.findParent(path, {it.endsWith('z')})

        then:
        parent == null
    }
}
