package org.zk.linkman.encurtador.tools;

import java.util.Random;
import java.util.function.Consumer;


public class ValuesUtils {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    public static <T> void setIf(T value, boolean conditional, Consumer<T> set) {
        if (conditional) set.accept(value);
    }

    public static Integer generateRandomCode(int size) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= size; i++) {
            int number = (int) (Math.random() * 9);
            sb.append(number);
        }

        return Integer.parseInt(sb.toString());
    }

    public static String generateRandomWord(int size) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {

            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}
