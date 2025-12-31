package teamdevhub.devhub.common.util;

import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
public class StringUtil {

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static String defaultIfEmpty(String str, String defaultStr) {
        return isEmpty(str) ? defaultStr : str;
    }

    public static String generateRandomUUID() {
        return UUID.randomUUID().toString();
    }

    public static String truncate(String str, int maxLength) {
        if (isEmpty(str)) return str;
        return str.length() <= maxLength ? str : str.substring(0, maxLength);
    }
}