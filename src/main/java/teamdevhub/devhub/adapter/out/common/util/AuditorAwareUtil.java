package teamdevhub.devhub.adapter.out.common.util;

import lombok.NonNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorAwareUtil")
public class AuditorAwareUtil implements AuditorAware<String> {

    public static final String SYSTEM = "system";
    public static final String ANONYMOUS_USER = "anonymousUser";

    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return Optional.of(SYSTEM);
        }

        if (!authentication.isAuthenticated()) {
            return Optional.of(SYSTEM);
        }

        String name = authentication.getName();

        if (ANONYMOUS_USER.equals(name)) {
            return Optional.of(SYSTEM);
        }

        return Optional.of(name);
    }
}