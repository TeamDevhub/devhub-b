package teamdevhub.devhub.adapter.out.common.util;

import lombok.NonNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.adapter.out.auth.userDetail.LoginAuthentication;
import teamdevhub.devhub.domain.common.record.auth.LoginUser;

import java.util.Optional;

@Component("auditorAwareUtil")
public class AuditorAwareUtil implements AuditorAware<String> {

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
        if (principal instanceof LoginAuthentication loginAuthentication) {
            return Optional.of(loginAuthentication.getUser().email());
        }

        if (principal instanceof LoginUser loginUser) {
            return Optional.of(loginUser.email());
        }

        return Optional.of(SYSTEM);
    }
}