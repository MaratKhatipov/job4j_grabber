package ru.job4j.grabber.html;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Parse;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {
    private static final int MAX_PAGES = 5;
    private static final String SOURCE_LINK = "https://career.habr.com";

    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer",
            SOURCE_LINK);

    private final DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    @Override
    public List<Post> list(String addr) {
        List<Post> posts = new ArrayList<>();
        for (int page = 1; page <= MAX_PAGES; page++) {
            posts = parsePage(posts, addr + "?page=" + page);
        }
        return posts;
    }

    private List<Post> parsePage(List<Post> posts, String addr) {
        Connection connection = Jsoup.connect(addr);
        Document document = null;
        try {
            document = connection.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements rows = document.select(".vacancy-card__inner");
        rows.forEach(row -> posts.add(parsePost(row)));
        return posts;
    }

    public Post parsePost(Element element) {
        Element titleElement = element.select(".vacancy-card__title").first();
        Element linkElement = titleElement.child(0);
        String vacancyName = titleElement.text();
        String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
        Element dateElement = element.select(".vacancy-card__date").first().child(0);
        LocalDateTime dt = dateTimeParser.parse(dateElement.attr("datetime"));
        String text = null;
        try {
            text = retrieveDescription(link);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Post(vacancyName, link, text, dt);
    }

    public String retrieveDescription(String link) throws IOException {
        Connection connection = Jsoup.connect(link);
        Document document = connection.get();
        Element row = document.selectFirst(".job_show_description__vacancy_description");
        Element descriptionElement = row.selectFirst(".style-ugc");
        return descriptionElement.text();
    }

    public static void main(String[] args) {
        HabrCareerParse habrCareerParse = new HabrCareerParse(new HabrCareerDateTimeParser());
        habrCareerParse.list(PAGE_LINK);
    }
}
