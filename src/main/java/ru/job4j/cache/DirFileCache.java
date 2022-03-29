package ru.job4j.cache;
/*
Создать программу, эмулирующее поведение данного кеша.
Программа должна считывать текстовые файлы из системы и выдавать текст при запросе имени файла.
Если в кеше файла нет. Кеш должен загрузить себе данные.
По умолчанию в кеше нет ни одного файла. Текстовые файл должны лежать в одной директории.
Пример: Names.txt, Address.txt - файлы в системе.
При запросе по ключу Names.txt - кеш должен вернуть содержимое файла Names.txt.

Важно! key это относительный путь к файлу в директории.
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DirFileCache extends AbstractCache<String, String> {

    private final String cachingDir;

    public DirFileCache(String cachingDir) {
        this.cachingDir = cachingDir;
    }

    @Override
    protected String load(String key) {
        String value = null;
        try {
            value = Files.readString(Path.of(cachingDir, key));
        } catch (IOException e) {
            System.out.println("Ошибка/либо файл не существует");
            e.printStackTrace();
        }
        return value;
    }
}
