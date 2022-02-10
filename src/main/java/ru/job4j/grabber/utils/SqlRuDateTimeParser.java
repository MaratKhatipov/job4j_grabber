package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.util.Map;
import java.time.format.DateTimeFormatter;

public class SqlRuDateTimeParser implements DateTimeParser {
    private static final Map<String, String> MONTHS = Map.ofEntries(
            Map.entry("вчера", LocalDateTime.now().minusDays(1)
                    .format(
                    DateTimeFormatter.ofPattern("dd M yy"))),
            Map.entry("сегодня", LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("dd M yy"))),
            Map.entry("янв", "1"),
            Map.entry("фев", "2"),
            Map.entry("мар", "3"),
            Map.entry("апр", "4"),
            Map.entry("май", "5"),
            Map.entry("июн", "6"),
            Map.entry("июл", "7"),
            Map.entry("авг", "8"),
            Map.entry("сен", "9"),
            Map.entry("окт", "10"),
            Map.entry("ноя", "11"),
            Map.entry("дек", "12"));

    private static final DateTimeFormatter FORMAT_PARSER1 =
            DateTimeFormatter.ofPattern("dd M yy HH:mm");

    @Override
    public LocalDateTime parse(String parse) {
        String parseDT = "";
        String[] dateSplit = parse.split("[, ]+");
        if (dateSplit.length == 4) {
            parseDT = String.join(
                    " ",
                    dateSplit[0], MONTHS.get(dateSplit[1]), dateSplit[2], dateSplit[3]);
        }
        if (dateSplit.length == 2) {
            String check = MONTHS.get(dateSplit[0]);
            parseDT = String.join(
                    " ",
                    MONTHS.get(dateSplit[0]), dateSplit[1]);
        }
        return LocalDateTime.parse(parseDT, FORMAT_PARSER1);
    }
}
