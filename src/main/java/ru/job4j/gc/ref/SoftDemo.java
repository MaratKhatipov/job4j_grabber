package ru.job4j.gc.ref;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/*
Объекты, на которые ссылаются безопасные ссылки,
удаляются только если JVM не хватает памяти,
т.е. они могут пережить более одной сборки мусора.

Есть контракт для данного типа ссылок:
GC гарантировано удалит с кучи все объекты,
доступные только по soft-ссылке,
перед тем как бросит OutOfMemoryError
 */
public class SoftDemo {
    public static void main(String[] args) {
        example1();
        example2();
    }

    /*
    Первый метод:
    не смотря на то, что мы за'null'уляем сильную ссылку,
    на объект остается безопасная ссылки,
    а это значит объект будет удален только при нехватке памяти.
     */
    private static void example1() {
        Object strong = new Object();
        SoftReference<Object> soft = new SoftReference<>(strong);
        strong = null;
        System.out.println(soft.get());
    }

    /*
    Второй метод:
    мы создаем много объектов, но на них ссылается безопасная ссылка.
    Если мы создадим большое количество объектов при малом хипе,
    мы увидим, что объекты начнут удаляться, т.к. станем не хватать памяти.
     */
    private static void example2() {
        List<SoftReference<Object>> objects = new ArrayList<>();
        for (int i = 0; i < 100_000_000; i++) {
            objects.add(new SoftReference<>(new Object() {
                private String value = String.valueOf(System.currentTimeMillis());

                @Override
                protected void finalize() throws Throwable {
                    System.out.println("Object removed!");
                }
            }));
        }
        System.gc();
        int liveObject = 0;
        for (SoftReference<Object> ref : objects) {
            Object object = ref.get();
            if (object != null) {
                liveObject++;
            }
        }
        System.out.println(liveObject);
    }
}
