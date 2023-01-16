package org.ethelred.games.core;

import java.security.SecureRandom;
import java.util.Random;
import java.util.function.Supplier;

public class RandomShortCodeSupplier implements Supplier<String> {

    Random r = new SecureRandom();

    @Override
    public String get() {
        return r.ints('A', 'Z' + 1)
                .limit(4)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
