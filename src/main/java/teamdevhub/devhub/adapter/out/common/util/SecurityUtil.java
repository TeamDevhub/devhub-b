package teamdevhub.devhub.adapter.out.common.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.port.out.common.CurrentUserProvider;

@Component
public class SecurityUtil implements CurrentUserProvider {

    @Override
    public String getCurrentUserGuid() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }
}
