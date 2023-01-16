package org.ethelred.games.nuo;

import com.fasterxml.jackson.annotation.JsonValue;

enum Color {
    RED('r'),
    BLUE('b'),
    GREEN('g'),
    YELLOW('y'),
    WILD('x');

    @JsonValue
    final char shortCode;

    Color(char shortCode) {
        this.shortCode = shortCode;
    }

    static Color fromCode(char code) {
        for (Color c : values()) {
            if (Character.toLowerCase(code) == c.shortCode) {
                return c;
            }
        }
        throw new IllegalArgumentException("Invalid color code " + code);
    }
}
