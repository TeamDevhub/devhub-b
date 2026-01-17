package teamdevhub.devhub.port.out.provider;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface DateTimeProvider {
    String formatDate(LocalDate date, String pattern);
    String formatDateTime(LocalDateTime dateTime, String pattern);
    LocalDate parseDate(String dateStr, String pattern);
    LocalDateTime parseDateTime(String dateTimeStr, String pattern);
    LocalDate today();
    LocalDateTime now();
}
