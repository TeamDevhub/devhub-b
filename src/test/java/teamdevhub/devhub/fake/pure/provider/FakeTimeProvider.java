package teamdevhub.devhub.fake.pure.provider;

import teamdevhub.devhub.port.out.provider.TimeProvider;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class FakeTimeProvider implements TimeProvider {

    private final LocalDateTime currentDateTime;

    public FakeTimeProvider(LocalDateTime initialDateTime) {
        this.currentDateTime = initialDateTime;
    }

    @Override
    public LocalDateTime now() {
        return currentDateTime;
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
}
