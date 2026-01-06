package teamdevhub.devhub.domain.common.util;

public class StringUtil {

    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }

        return str.isBlank();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static String defaultIfEmpty(String str, String defaultStr) {
        if (isEmpty(str)) {
            return defaultStr;
        }

        return str;
    }

    public static String truncate(String str, int maxLength) {
        if (isEmpty(str)) {
            return str;
        }

        if (maxLength < 0) {
            throw new IllegalArgumentException("maxLength must be positive");
        }

        if (str.length() <= maxLength) {
            return str;
        }

        return str.substring(0, maxLength);
    }
}