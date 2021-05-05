package org.ethelred.games.nuo

import groovy.json.JsonSlurper
import org.ethelred.games.server.Main
import picocli.CommandLine
import spock.lang.AutoCleanup
import spock.lang.Specification

import java.util.function.Consumer

class ServerTest extends Specification {
    @AutoCleanup
    def server = new Main()

    @AutoCleanup
    def gru = Gru.create(Http.create(this))
                .prepare("http://localhost:7000")

    def setup() {
        def cmd = new CommandLine(server)
        cmd.execute()
    }


    def 'get list of games'() {
        expect:
            gru.test {
                get '/api/games'
                expect {
                    json inline('["nuo"]')
                }
            }
    }

    def 'should get a player cookie'() {
        expect:
            gru.test {
                get '/api/games'
                expect {
                    cookie {
                        name('javalin-cookie-store')
                    }
                }
            }

    }
}

class JavalinCookieStoreMinion extends AbstractMinion<Client> {

    def test
    protected JavalinCookieStoreMinion(Consumer test) {
        super(Client)
        this.test = test
    }

    @Override
    int getIndex() {
        return COOKIES_MINION_INDEX + 1
    }

    @Override
    protected void doVerify(Client client, Squad squad, GruContext context) throws Throwable {
        def cookies = squad.ask(CookieMinion) {getResponseCookies() }
        def cookieStoreCookie = cookies.find { it.name == 'javalin-cookie-store'}
        def cookieStore = Base64.decoder.decode(cookieStoreCookie.value)
    }
}
