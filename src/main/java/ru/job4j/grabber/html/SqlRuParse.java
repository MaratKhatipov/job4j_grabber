package ru.job4j.grabber.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Parse;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse implements Parse {

    private final DateTimeParser dateTimeParser;

    public SqlRuParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    @Override
    public List<Post> list(String link) {
        List<Post> postList = new ArrayList<>();
            try {
                for (int i = 1; i < 6; i++) {
                    Document doc = Jsoup.connect(link + i).get();
                    assert doc != null;
                    Elements row = doc.select(".postslisttopic");
                    for (Element td : row) {
                        Element href = td.child(0);
                        String title = td.child(0).text().toLowerCase();
                        if (title.contains("java") && !title.contains("javascript")) {
                            postList.add(detail(href.attr("href")));
                        }
                    }
                }
            } catch (IOException e) {
                    e.printStackTrace();
            }
            return postList;
    }

    @Override
    public Post detail(String link) {
        Document document = null;
        try {
            document = Jsoup.connect(link).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements row = document.select("td.msgBody");
        Elements titleData = document.select(".messageHeader");

        String[] arr = document.select(".msgFooter").get(0).text().split("\\[+");
        LocalDateTime created = dateTimeParser.parse(arr[0]);

        String title = titleData.parents().get(0).text();
        String description = row.get(1).text();
        return new Post(title, link, description, created);
    }

    public static void main(String[] args) throws IOException {
        String link = "https://www.sql.ru/forum/job-offers/";

        SqlRuDateTimeParser sqlRuDateTimeParser = new SqlRuDateTimeParser();
        SqlRuParse sqlRuParse = new SqlRuParse(sqlRuDateTimeParser);

        List<Post> postList = sqlRuParse.list(link);

        System.out.println(postList.size());
        for (Post s : postList) {
            System.out.println(s);
        }
    }
}
