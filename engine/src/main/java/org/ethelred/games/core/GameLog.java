package org.ethelred.games.core;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.ArrayList;
import java.util.List;

public class GameLog {
    private final int maxSize;

    @JsonValue
    private final List<LogEntry> q;

    public GameLog(int size) {
        maxSize = size;
        q = new ArrayList<>(size);
    }
    record LogEntry(String playerName, String actionName, String actionValue){}

    void push(Player player, Action action) {
        while (q.size() >= maxSize) {
            q.remove(0);
        }
        q.add(new LogEntry(player.name(), action.name(), action.argumentAsString()));
    }
}
