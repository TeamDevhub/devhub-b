package teamdevhub.devhub.outbound.common.persistence.jpa.audit;

import lombok.NonNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.outbound.security.auth.UserAuthentication;
import teamdevhub.devhub.core.auth.domain.UserCredential;

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
            return Optional.of(userAuthentication.getUser().loginId());
        }

        if (principal instanceof UserCredential userCredential) {
            return Optional.of(userCredential.loginId());
        }

        return Optional.of(SYSTEM);
    }
}