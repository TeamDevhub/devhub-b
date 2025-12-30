package teamdevhub.devhub.common.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    public static String getCurrentUserGuid() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }
}
