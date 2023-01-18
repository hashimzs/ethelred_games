package org.ethelred.games.server;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import io.javalin.config.JavalinConfig;
import org.eclipse.jetty.proxy.ProxyServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;

@Module
public class ProdProfile implements Profile
{
    @Provides
    @IntoMap
    @StringKey("production")
    public static Profile get() {
        return new ProdProfile();
    }

    @Override
    public int getPort()
    {
        return 7000;
    }

    @Override
    public void configureServer(JavalinConfig javalinConfig)
    {
        // attach proxy to node
        javalinConfig.jetty.server(this::configureJetty);
    }

    private Server configureJetty() {
        var server = new Server();
        var wrapper = new NonApiWrapper();
        var context = new ServletContextHandler();
        var holder = context.addServlet(ProxyServlet.Transparent.class, "/");
        holder.setInitParameter("proxyTo", "http://localhost:3000");
        wrapper.setHandler(context);
        server.setHandler(new HandlerCollection(wrapper));
        return server;
    }

    @Override
    public boolean runNode() {
        return true;
    }
}
