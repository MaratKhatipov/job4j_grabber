package ru.job4j.grabber.utils;

import org.junit.Test;

import java.time.LocalDateTime;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
public class SqlRuDateTimeParserTest {

    @Test
    public void whenParseJanThenJan() {
        SqlRuDateTimeParser test = new SqlRuDateTimeParser();
        String dateTest = "20 янв 22, 09:40";
        LocalDateTime actual = test.parse(dateTest);
        LocalDateTime expected = LocalDateTime.of(2022, 1, 20, 9, 40);
        assertThat(actual, is(expected));
    }

    @Test
    public void whenParseYesterdayThenYesterday() {
        SqlRuDateTimeParser test = new SqlRuDateTimeParser();
        String test1 = "вчера, 05:08";
        LocalDateTime actual = test.parse(test1);
        LocalDateTime expected = LocalDateTime.of(2022, 2, 15, 5, 8);
        assertThat(actual, is(expected));
    }

    @Test
    public void whenParseToday() {
        SqlRuDateTimeParser test = new SqlRuDateTimeParser();
        String test1 = "сегодня, 05:08";
        LocalDateTime actual = test.parse(test1);
        LocalDateTime expected = LocalDateTime.of(2022, 2, 16, 5, 8);
        assertThat(actual, is(expected));
    }
}