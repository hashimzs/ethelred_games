package org.ethelred.games.bot;

import com.github.javafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;

public class RandomPlayer implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Faker faker = new Faker();
    private static final Comparator<? super BotApi.Action> CHOICE_COMPARATOR = Comparator.comparing(RandomPlayer::scoreAction);
    private static final Set<String> EXCLUDED_ACTIONS = Set.of("addBot");

    private static int scoreAction(BotApi.Action action) {
        if ("playCard".equals(action.name())) {
            return 1;
        }
        if ("true".equals(action.value())) {
            return 2;
        }
        return 10;
    }


    private final Random random;
    private final String shortCode;
    private final BotApi api;
    private final String name;

    private volatile boolean running = true;

    public RandomPlayer(Random random, String shortCode, BotApi api) {
        this.random = random;
        this.shortCode = shortCode;
        this.api = api;
        this.name = getName();
    }

    private String getName() {
        var name = faker.harryPotter().character();
        var first = name.split(" ")[0];
        return first + " [Bot]";
    }

    @Override
    public void run() {

        try {
            var view = api.joinGame(shortCode);
            setName();
            while (running) {
                try {
                    Thread.sleep(random.nextInt(300, 1100));
                    view = act(view);
                } catch (InterruptedException e) {
                    //
                }
            }
            LOGGER.info("Exiting");
        } catch (Exception e) {
            LOGGER.warn("Unhandled exception, exiting.", e);
        }
    }

    private BotApi.GameResponse act(BotApi.GameResponse view) {
        var defs = view.playerView().availableActions();
        if (defs == null || defs.isEmpty()) {
            return api.poll(view.path());
        }
        var choices = defs.stream()
                .flatMap(actionDefinition -> {
                    if (actionDefinition.possibleArguments().isEmpty()) {
                        return Stream.of(new BotApi.Action(actionDefinition.name(), ""));
                    } else {
                        return actionDefinition.possibleArguments().stream()
                                .map(arg -> new BotApi.Action(actionDefinition.name(), arg));
                    }
                })
                .filter(action -> !EXCLUDED_ACTIONS.contains(action.name()))
                .sorted(CHOICE_COMPARATOR)
                .toList();
        if (choices.isEmpty()) {
            return api.poll(view.path());
        }
        var choice = choices.get(0);
        LOGGER.info("{}: {} {}", name, choice.name(), choice.value());
        return api.action(view.path(), choice);
    }

    private void setName() {
        api.setName(name);
    }

    public void stop() {
        running = false;
    }
}
