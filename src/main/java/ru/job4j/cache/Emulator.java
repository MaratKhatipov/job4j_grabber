package ru.job4j.cache;

/*
Создать класс Emulator для работы с пользователем.
Предоставить пользователю возможности:
- указать кэшируемую директорию
- загрузить содержимое файла в кэш
- получить содержимое файла из кэша
 */

import java.util.Scanner;

public class Emulator {
    public static void main(String[] args) {
        AbstractCache<String, String> dirCache = null;
        Scanner scanner = new Scanner(System.in);
        boolean flag = true;
        while (flag) {
            if (dirCache == null) {
                System.out.println("Введите директорию");
                dirCache = new DirFileCache(scanner.nextLine());
                continue;
            }
            System.out.println("Введите название файла или \"Q\" для выхода");
            String file1 = scanner.nextLine();
            if ("Q".equals(file1)) {
                flag = false;
                continue;
            }
            System.out.println("Содержимое файла: \n " + dirCache.get(file1));
        }
    }
}