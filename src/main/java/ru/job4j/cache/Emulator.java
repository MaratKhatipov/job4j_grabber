package ru.job4j.cache;

/*
Создать класс Emulator для работы с пользователем.
Предоставить пользователю возможности:
- указать кэшируемую директорию
- загрузить содержимое файла в кэш
- получить содержимое файла из кэша
 */

public class Emulator {
    public static void main(String[] args) {

        String path = "D:/projects/job4j_grabber";
        String file1 = "Names.txt";
        String file2 = "Address.txt";

        DirFileCache dirCache = new DirFileCache(path);
        System.out.println(dirCache.get(file1) + System.lineSeparator());
        System.out.println(dirCache.get(file2) + System.lineSeparator());
    }
}
