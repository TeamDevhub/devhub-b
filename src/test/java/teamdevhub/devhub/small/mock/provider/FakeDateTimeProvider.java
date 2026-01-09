package teamdevhub.devhub.small.mock.provider;

import teamdevhub.devhub.common.provider.datetime.DateTimeProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FakeDateTimeProvider implements DateTimeProvider {

    private LocalDateTime fixedDateTime;

    public FakeDateTimeProvider(LocalDateTime fixedDateTime) {
        this.fixedDateTime = fixedDateTime;
    }

    public FakeDateTimeProvider() {

    }

    @Override
    public String formatDate(LocalDate date, String pattern) {
        return "";
    }

    @Override
    public String formatDateTime(LocalDateTime dateTime, String pattern) {
        return "";
    }

    @Override
    public LocalDate parseDate(String dateStr, String pattern) {
        return null;
    }

    @Override
    public LocalDateTime parseDateTime(String dateTimeStr, String pattern) {
        return null;
    }

    @Override
    public LocalDate today() {
        return null;
    }

    @Override
    public LocalDateTime now() {
        return fixedDateTime;
    }
}
