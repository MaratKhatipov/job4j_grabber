package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SqlRuParse {
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements row = doc.select(".postslisttopic");
        for (Element td : row) {
            Element parent = td.parent();
//            System.out.println(parent.tag());
//            System.out.println(parent.text());
//            System.out.println("колличество детей " + parent.children().size());
//            System.out.println("дата - " + parent.children().get(5).text());
            System.out.println(parent.child(5).text());
        }

    }
}
