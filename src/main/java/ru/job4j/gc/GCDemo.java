package ru.job4j.gc;

import ru.job4j.gc.demo.UserDemo;

public class GCDemo {

    private static final long KB = 1000;
    private static final long MB = KB * KB;
    private static final Runtime ENVIRONMENT = Runtime.getRuntime();

    public static void info() {
        UserDemo.runtime(ENVIRONMENT, MB);
    }

    public static void main(String[] args) {
        info();
        for (int i = 0; i < 10; i++) {
            new Person(i, "N" + i);
        }
        System.gc();
        info();
    }
}
