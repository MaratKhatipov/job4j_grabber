package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.util.Map;

import java.time.format.DateTimeFormatter;

public class SqlRuDateTimeParser implements DateTimeParser {

    public static final int LENGTH_IF_2_PARAMETERS = 2;
    public static final int ANOTHER_DAY_LENGTH = 4;

    private static final DateTimeFormatter FORMAT_PARSER1 =
            DateTimeFormatter.ofPattern("d M yy HH:mm");
    private static final DateTimeFormatter FORMAT_PARSER2 =
            DateTimeFormatter.ofPattern("dd M yy");

    private static final String YESTERDAY = "вчера";
    private static final String TODAY = "сегодня";

    private static final Map<String, String> MONTHS = Map.ofEntries(
            Map.entry(YESTERDAY, LocalDateTime.now().minusDays(1).format(FORMAT_PARSER2)),
            Map.entry(TODAY, LocalDateTime.now().format(FORMAT_PARSER2)),
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

    /**
     * @param parse входная строка с датой с сайта
     *
     * lengthIf2Parameters - два параметра в обозначении даты
     * (например: сегодня, 02:30 или вчера, 19:26)
     *
     * anotherDayLength - остальные обозначения даты
     *
     * @return преобразованую строку в дату
     */
    @Override
    public LocalDateTime parse(String parse) {
        String parseDT = "";
        String[] dateSplit = parse.split("[, ]+");
        if (dateSplit.length == ANOTHER_DAY_LENGTH) {
            parseDT = String.join(
                    " ",
                    dateSplit[0], MONTHS.get(dateSplit[1]), dateSplit[2], dateSplit[3]);
        }
        if (dateSplit.length == LENGTH_IF_2_PARAMETERS) {
            parseDT = String.join(
                    " ",
                    MONTHS.get(dateSplit[0]), dateSplit[1]);
        }
        return LocalDateTime.parse(parseDT, FORMAT_PARSER1);
    }
}
