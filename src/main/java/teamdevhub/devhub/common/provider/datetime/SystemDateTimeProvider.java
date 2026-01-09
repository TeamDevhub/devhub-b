package teamdevhub.devhub.common.provider.datetime;

import teamdevhub.devhub.common.util.StringUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SystemDateTimeProvider implements DateTimeProvider {

    public String formatDate(LocalDate date, String pattern) {
        if (date == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return date.format(formatter);
    }

    public String formatDateTime(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }

    public LocalDate parseDate(String dateStr, String pattern) {
        if (StringUtil.isEmpty(dateStr)) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(dateStr, formatter);
    }

    public LocalDateTime parseDateTime(String dateTimeStr, String pattern) {
        if (StringUtil.isEmpty(dateTimeStr)) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(dateTimeStr, formatter);
    }

    public LocalDate today() {
        return LocalDate.now();
    }

    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}