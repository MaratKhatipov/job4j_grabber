package ru.job4j.grabber.utils;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HabrCareerDateTimeParserTest {

    @Test
    public void whenParseJanThenJan() {
        HabrCareerDateTimeParser test = new HabrCareerDateTimeParser();
        String dateTest = "20 янв 22, 09:40";
        LocalDateTime actual = test.parse(dateTest);
        LocalDateTime expected = LocalDateTime.of(2022, 1, 20, 9, 40);
        assertThat(actual, is(expected));
    }

    @Test
    public void whenParseYesterdayThenYesterday() {
        HabrCareerDateTimeParser test = new HabrCareerDateTimeParser();
        String test1 = "вчера, 05:08";
        LocalDateTime actual = test.parse(test1);
        LocalDateTime expected = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(5, 8));
        assertThat(actual, is(expected));
    }

    @Test
    public void whenParseToday() {
        HabrCareerDateTimeParser test = new HabrCareerDateTimeParser();
        String test1 = "сегодня, 05:08";
        LocalDateTime actual = test.parse(test1);
        LocalDateTime expected = LocalDateTime.of(LocalDate.now(), LocalTime.of(5, 8));
        assertThat(actual, is(expected));
    }
}