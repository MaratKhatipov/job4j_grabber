package ru.job4j.grabber.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.time.LocalDateTime;

public class SqlRuParse {
    public static void main(String[] args) throws IOException {
        for (int i = 1; i < 6; i++) {
            Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers/" + i).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                Element href = td.child(0);
                Element parent = td.parent();
                System.out.println(href.text());
                System.out.println(href.attr("href"));
                System.out.println(parent.child(5).text() + System.lineSeparator());
            }
        }
        String link = "https://www.sql.ru/forum/"
                + "1325330/lidy-be-fe-senior-cistemnye-analitiki-qa-i-devops-moskva-do-200t";
        System.out.println(detail(link));

        String link2 = "https://www.sql.ru/forum/1342124/v-poiskah-java-developer";
        System.out.println(detail(link2));
    }

    public static Post detail(String link) throws IOException {
        Post post = new Post();
        Document document = Jsoup.connect(link).get();
        Elements row = document.select("td.msgBody");
        Elements titleData = document.select(".messageHeader");
        post.setLink(link);
        post.setTitle(titleData.parents().get(0).text());
        post.setDescription(row.get(1).text());
        post.setId(Integer.parseInt(
                document.select("td.msgFooter").get(0).child(0).text()));
        String[] arr = document.select(".msgFooter").get(0).text().split("\\[+");
        SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
        post.setCreated(parser.parse(arr[0]));
        return post;
    }
}
