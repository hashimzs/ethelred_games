package org.ethelred.games.nuo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

public record Card(Color color, char code) implements Comparable<Card> {
    public Card
    {
        var t = Type.fromCode(code);
        if (t.isWild() != (color == Color.WILD))
        {
            throw new IllegalArgumentException("Invalid combination");
        }
    }

    @Override
    public int compareTo(@NotNull Card o) {
        return shortCode().compareTo(o.shortCode());
    }

    enum Type {
        NUMBER(),
        WILD('w') {
            @Override
            boolean isWild()
            {
                return true;
            }
        },
        DRAW_FOUR('x') {
            @Override
            boolean isWild()
            {
                return true;
            }
        },
        SKIP('s'),
        REVERSE('r'),
        DRAW_TWO('d');

        private final char shortCode;

        Type(char shortCode)
        {
            this.shortCode = shortCode;
        }

        Type()
        {
            this.shortCode = '0';
        }

        char code()
        {
            return shortCode;
        }

        boolean isWild()
        {
            return false;
        }

        static Type fromCode(char code)
        {
            for (Type t :
                    values())
            {
                if (code == t.shortCode)
                {
                    return t;
                }
            }
            if (Character.isDigit(code))
            {
                return NUMBER;
            }
            throw new IllegalArgumentException("Invalid type code " + code);
        }
    }

    enum Color {
        RED('r'),
        BLUE('b'),
        GREEN('g'),
        YELLOW('y'),
        WILD('x');

        @JsonValue
        private final char shortCode;

        Color(char shortCode)
        {
            this.shortCode = shortCode;
        }

        static Color fromCode(char code)
        {
            for (Color c: values())
            {
                if (Character.toLowerCase(code) == c.shortCode)
                {
                    return c;
                }
            }
            throw new IllegalArgumentException("Invalid color code " + code);
        }
    }

    @JsonValue
    public String shortCode()
    {
        return String.valueOf(color.shortCode) + code;
    }

    public Type type()
    {
        return Type.fromCode(code);
    }

    @JsonCreator
    public static Card fromCode(String cardCode)
    {
        if (cardCode.length() == 2)
        {
            var c = Color.fromCode(cardCode.charAt(0));
            var s = cardCode.charAt(1);
            var t = Type.fromCode(s);
            if (t.isWild() == (c == Color.WILD))
            {
                return new Card(c, s);
            }
        }
        throw new IllegalArgumentException("Invalid card code " + cardCode);
    }
}
