package com.jbsoft.unoserver.utility;

import java.util.Random;

public class Randomizer {
    private static Random random = new Random();

    public static String generateID() {
        int leftLimit = 65; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 5;

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(a -> a < 91 || a > 96)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }
}
