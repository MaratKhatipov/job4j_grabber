package ru.job4j.gc.ref;

import java.lang.ref.SoftReference;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class StrongDemo {
    public static void main(String[] args) throws InterruptedException {
        example3();
    }

    /*
     Мы создаем объект и далее их за'null'яем.
     Вызываем сборщик мусора и ждем некоторое время.
     Объекты удаляются, т.к. ссылки на них null
     */
    private static void example1() throws InterruptedException {
        Object[] objects = new Object[100];
        for (int i = 0; i < 100; i++) {
            objects[i] = new Object() {
                @Override
                protected void finalize() throws Throwable {
                    System.out.println("Object removed!");
                }
            };
        }
        for (int i = 0; i < 100; i++) {
            objects[i] = null;
        }
        System.gc();
        TimeUnit.SECONDS.sleep(5);
    }

    /*
    мы создаем объекты вместе с вложенными.
    Удаляя внешние объекты как в примере выше удаляются и вложенные,
    не смотря на то что они не null.
     */
    private static void example2() throws InterruptedException {
        Object[] objects = new Object[100];
        for (int i = 0; i < 100; i++) {
            Object object = new Object() {
                private final Object innerObject = new Object() {
                    @Override
                    protected void finalize() throws Throwable {
                        System.out.println("Remove inner object!");
                    }
                };
            };
            objects[i] = object;
        }
        for (int i = 0; i < 100; i++) {
            objects[i] = null;
        }
        System.gc();
        TimeUnit.SECONDS.sleep(5);
    }

    private static void example3() {
        List<String> strings = new ArrayList<>();
        SoftReference<List> soft = new SoftReference<>(strings);
        while (true) {
            strings.add(String.valueOf(System.currentTimeMillis()));
            System.out.println(soft.get());
        }
    }
}
