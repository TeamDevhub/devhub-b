package teamdevhub.devhub.adapter.out.common.util;

import org.springframework.stereotype.Component;
import teamdevhub.devhub.domain.common.util.StringUtil;
import teamdevhub.devhub.port.out.provider.DateTimeProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeUtil implements DateTimeProvider {

    @Override
    public String formatDate(LocalDate date, String pattern) {
        if (date == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return date.format(formatter);
    }

    @Override
    public String formatDateTime(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }

    @Override
    public LocalDate parseDate(String dateStr, String pattern) {
        if (StringUtil.isEmpty(dateStr)) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(dateStr, formatter);
    }

    @Override
    public LocalDateTime parseDateTime(String dateTimeStr, String pattern) {
        if (StringUtil.isEmpty(dateTimeStr)) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(dateTimeStr, formatter);
    }

    @Override
    public LocalDate today() {
        return LocalDate.now();
    }

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}