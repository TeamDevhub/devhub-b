package teamdevhub.devhub.common.component;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorProvider")
public class AuditorAwareImpl implements AuditorAware<String> {

    public static final String SYSTEM = "system";

    @Override
    public Optional<String> getCurrentAuditor() {


        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return Optional.of(SYSTEM);
        }

        if (!authentication.isAuthenticated()) {
            return Optional.of(SYSTEM);
        }

        String name = authentication.getName();

        if ("anonymousUser".equals(name)) {
            return Optional.of(SYSTEM);
        }

        return Optional.of(name);
    }
}