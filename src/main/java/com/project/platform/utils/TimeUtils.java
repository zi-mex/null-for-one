package com.project.platform.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TimeUtils {
    private static final DateTimeFormatter FORMATTER_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter FORMATTER_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static List<LocalDateTime> getRecentSevenDays(Integer num) {
        List<LocalDateTime> dates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 从今天开始，获取前7天的日期
        LocalDateTime today = LocalDateTime.now();
        for (int i = 0; i < num; i++) {
            LocalDateTime date = today.plusDays(-i);
            String formattedDate = date.format(formatter);
            dates.add(date);
        }
        // 将列表反转，使最新的日期在列表末尾
        java.util.Collections.reverse(dates);
        return dates;
    }

    public static String formatterDate(LocalDateTime localDateTime) {
        return formatterDate(localDateTime, FORMATTER_DATE);
    }

    public static String formatterDate(LocalDateTime localDateTime, String formatter) {
        return formatterDate(localDateTime, DateTimeFormatter.ofPattern(formatter));
    }

    public static String formatterDate(LocalDateTime localDateTime, DateTimeFormatter formatter) {
        return localDateTime.format(formatter);
    }

    public static String formatterTime(LocalDateTime localDateTime) {
        return formatterTime(localDateTime, FORMATTER_TIME);

    }

    public static String formatterTime(LocalDateTime localDateTime, String formatter) {
        return formatterTime(localDateTime, DateTimeFormatter.ofPattern(formatter));
    }

    public static String formatterTime(LocalDateTime localDateTime, DateTimeFormatter formatter) {
        return localDateTime.format(formatter);
    }

    /**
     * 将 LocalDateTime 设置为当天的凌晨
     *
     * @param dateTime 输入的 LocalDateTime 对象
     * @return 设置为当天凌晨的 LocalDateTime 对象
     */
    public static LocalDateTime setToMidnight(LocalDateTime dateTime) {
        return dateTime.withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    /**
     * 将 LocalDateTime 设置为当天的 23:59:59
     * @param dateTime 输入的 LocalDateTime 对象
     * @return 设置为当天 23:59:59 的 LocalDateTime 对象
     */
    public static LocalDateTime setToEndOfDay(LocalDateTime dateTime) {
        return dateTime.withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(59);
    }
}

