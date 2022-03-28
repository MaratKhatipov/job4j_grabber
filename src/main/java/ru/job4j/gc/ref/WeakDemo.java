package ru.job4j.gc.ref;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
Объекты, на которые ссылаются слабые ссылки удаляются сразу,
если на них нет сильных или безопасных ссылок
 */
public class WeakDemo {
    public static void main(String[] args) throws InterruptedException {
        ex1();
        ex2();
        example3();
    }
    /*
     Первый метод:
     за'null'ение сильной ссылки приводит к удалению объекта
     и мы его также уже не можем получить по слабой ссылке.
     */

    private static void ex1() throws InterruptedException {
        Object object = new Object() {
            @Override
            protected void finalize() throws Throwable {
                System.out.println("Removed");
            }
        };
        WeakReference weak = new WeakReference<>(object);
        object = null;
        System.gc();
        TimeUnit.SECONDS.sleep(3);
        System.out.println(weak.get());
    }

    /*
    Второй метод:
    мы создаем объект вообще без сильных ссылки.
    При вызове сборщика мусора они все удаляются.
     */
    private static void ex2() throws InterruptedException {
        List<WeakReference<Object>> objects = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            objects.add(new WeakReference<Object>(new Object() {
                @Override
                protected void finalize() throws Throwable {
                    System.out.println("Removed!!!!");
                }
            }));
        }
        System.gc();
        TimeUnit.SECONDS.sleep(3);
    }

    /*
    ReferenceQueue
    Все типы ссылок, за исключением сильных,
    в Java являются наследниками класса Reference.
    Все его наследники всегда попадают в ReferenceQueue.
    WearReference будет помещена в очередь ReferenceQueue
    и мы можем пока объект не удален физически получить его из этой очереди.
     */
    private static void example3() throws InterruptedException {
        Object object = new Object() {
            @Override
            protected void finalize() throws Throwable {
                System.out.println("Removed example3");
            }
        };
        ReferenceQueue<Object> queue = new ReferenceQueue<>();
        WeakReference<Object> weak = new WeakReference<>(object, queue);
        object = null;

        System.gc();

        TimeUnit.SECONDS.sleep(3);
        System.out.println("from link " + weak.get());
        System.out.println("from queue " + queue.poll());
    }
}
