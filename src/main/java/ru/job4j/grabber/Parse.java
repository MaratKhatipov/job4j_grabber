package ru.job4j.grabber;

import ru.job4j.grabber.model.Post;

import java.io.IOException;
import java.util.List;

public interface Parse {
    /**
     * Этот метод загружает список объявлений по ссылке типа -
     * "https://www.sql.ru/forum/job-offers/1 "
     * @param link ссылка основной страницы с которой начинать парсинг сайта
     * @return
     * @throws IOException
     */
    List<Post> list(String link);

    /**
     * Этот метод загружает детали объявления по ссылке типа -
     * "https://www.sql.ru/forum/1342124/v-poiskah-java-developer"
     * Непосредственна сама страница вакансии/topic с описанием
     * Производит запись в экземпляр класса Post
     * @param link
     * @return
     * @throws IOException
     */
    Post detail(String link);
}
