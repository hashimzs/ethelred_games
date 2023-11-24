package org.ethelred.games.typeinfo;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonBooleanFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ethelred.games.server.bind.EngineModule;
import picocli.CommandLine;

import java.lang.reflect.Proxy;
import java.util.List;

@CommandLine.Command(name = "typeinfo", mixinStandardHelpOptions = true)
public class Main implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger();
    @CommandLine.Parameters(arity = "1", defaultValue = "org.ethelred.games.nuo.NuoPlayerView")
    private String rootClassName;

    public static void main(String[] args) {
        new CommandLine(new Main()).execute(args);
    }

    @Override
    public void run() {
        try {
            var klass = Class.forName(rootClassName);
            TypeInfo.visitClass(klass);
        } catch (ClassNotFoundException | JsonMappingException e) {
            throw new RuntimeException(e);
        }
    }
}
