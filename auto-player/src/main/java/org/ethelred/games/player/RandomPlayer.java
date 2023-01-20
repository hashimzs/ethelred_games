package org.ethelred.games.player;

import com.github.javafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;
import java.util.Random;
import java.util.stream.Stream;

public class RandomPlayer implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Faker faker = new Faker();
    private static final Comparator<? super GameApi.Action> CHOICE_COMPARATOR = Comparator.comparing(RandomPlayer::scoreAction);

    private static int scoreAction(GameApi.Action action) {
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
    private final GameApi api;
    private final String name;

    public RandomPlayer(Random random, String shortCode, GameApi api) {
        this.random = random;
        this.shortCode = shortCode;
        this.api = api;
        this.name = faker.harryPotter().character();
    }

    @Override
    public void run() {

        try {
            var view = api.joinGame(shortCode);
            setName();
            while (true) {
                try {
                    Thread.sleep(random.nextInt(100, 1000));
                    view = act(view);
                } catch (InterruptedException e) {
                    //
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Unhandled exception, exiting.", e);
        }
    }

    private GameApi.GameResponse act(GameApi.GameResponse view) {
        var defs = view.playerView().availableActions();
        if (defs == null || defs.isEmpty()) {
            return api.poll(view.path());
        }
        var choices = defs.stream()
                .flatMap(actionDefinition -> {
                    if (actionDefinition.possibleArguments().isEmpty()) {
                        return Stream.of(new GameApi.Action(actionDefinition.name(), ""));
                    } else {
                        return actionDefinition.possibleArguments().stream()
                                .map(arg -> new GameApi.Action(actionDefinition.name(), arg));
                    }
                })
                .sorted(CHOICE_COMPARATOR)
                .toList();
        var choice = choices.get(0);
        LOGGER.info("{}: {} {}", name, choice.name(), choice.value());
        return api.action(view.path(), choice);
    }

    private void setName() {
        api.setName(name);
    }
}
