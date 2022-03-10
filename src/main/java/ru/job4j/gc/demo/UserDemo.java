package ru.job4j.gc.demo;

import ru.job4j.gc.Person;

public class UserDemo {

    private static final long KB = 1024;
    private static final long MB = KB * KB;
    private static final Runtime ENVIRONMENT = Runtime.getRuntime();

    public static void info() {
        runtime(ENVIRONMENT, MB);
    }

    public static void runtime(Runtime environment, long mb) {
        final long fMemory = environment.freeMemory();
        final long tlMemory = environment.totalMemory();
        final long mMemory = environment.maxMemory();
        System.out.println("=== Environment state ===");
        System.out.printf("Free: %d%n", fMemory / MB);
        System.out.printf("Total: %d%n", tlMemory / MB);
        System.out.printf("Max: %d%n", mMemory / MB);
    }

    public static void main(String[] args) {
        info();
        User user = new User(50, "Petya");
        System.out.println(user);
        for (int i = 0; i < 9000; i++) {
//            System.out.println("\r" + i);
            new User(i, "Petya");
        }
        System.gc();
        info();
    }
}
