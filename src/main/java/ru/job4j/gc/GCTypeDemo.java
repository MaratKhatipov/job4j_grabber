package ru.job4j.gc;

import java.util.Random;

public class GCTypeDemo {
    public static void main(String[] args) {
        Random random = new Random();
        int length = 100;
        String[] data = new String[100];
        for (int i = 0; i < 100; i = (i + 1) % data.length) {
            data[i] = String.valueOf(
                    (char) random.nextInt(255)
            ).repeat(length);
        }
    }
}
