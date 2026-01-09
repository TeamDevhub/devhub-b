package teamdevhub.devhub.adapter.out.common.provider.audit;

import lombok.NonNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.common.web.security.auth.UserAuthentication;
import teamdevhub.devhub.domain.common.vo.auth.AuthenticatedUser;

import java.util.Optional;

@Component("auditorAwareProvider")
public class AuditorAwareProvider implements AuditorAware<String> {

    public static final String SYSTEM = "system";
    public static final String ANONYMOUS_USER = "anonymousUser";

    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                ANONYMOUS_USER.equals(authentication.getName())) {
            return Optional.of(SYSTEM);
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserAuthentication userAuthentication) {
            return Optional.of(userAuthentication.getUser().email());
        }

        if (principal instanceof AuthenticatedUser authenticatedUser) {
            return Optional.of(authenticatedUser.email());
        }

        return Optional.of(SYSTEM);
    }
}