package org.ethelred.games.player;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import org.apache.logging.log4j.LogManager;
import picocli.CommandLine;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@CommandLine.Command(mixinStandardHelpOptions = true)
public class Main implements Runnable {
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();

    private final Random random = new SecureRandom();
    @CommandLine.Option(names = {"-u", "--base"}, description = "Base for URL", defaultValue = "http://localhost:7000")
    private String urlBase;

    @CommandLine.Option(names = {"-c", "--code"}, description = "Short code for game to join", required = true)
    private String shortCode;

    @CommandLine.Option(names = {"-p", "--players"}, description = "How many players to run", defaultValue = "1")
    private int playerCount;

    public static void main(String[] args) {
        new CommandLine(new Main()).execute(args);
    }

    private GameApi api;
    private void setupApi() {
        var jar = new CookieJar();
        var mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                ;
        api = Feign.builder()
                .logger(new Slf4jLogger("Feign"))
                .logLevel(Logger.Level.FULL)
                .decoder(new JacksonDecoder(mapper))
                .encoder(new JacksonEncoder(mapper))
                .requestInterceptor(jar)
                .responseInterceptor(jar)
                .target(GameApi.class, urlBase);
    }

    @Override
    public void run() {
        setupApi();
        var futures = new ArrayList<Future<?>>();
        var executor = Executors.newFixedThreadPool(playerCount);
        for (int i = 0; i < playerCount; i++) {
            var player = new RandomPlayer(random, shortCode, api);
            futures.add(executor.submit(player));
        }
        for (var f :
                futures) {
            try {
                f.get();
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.warn("Exception from task", e);
            }
        }
        executor.shutdown();
    }
}
