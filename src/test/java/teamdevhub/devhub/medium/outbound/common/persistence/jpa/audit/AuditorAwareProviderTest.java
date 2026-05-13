package teamdevhub.devhub.medium.outbound.common.persistence.jpa.audit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.outbound.common.persistence.jpa.audit.AuditorAwareProvider;
import teamdevhub.devhub.outbound.security.auth.UserAuthentication;
import teamdevhub.devhub.core.user.domain.vo.UserRole;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class AuditorAwareProviderTest {

    private AuditorAwareProvider auditorAwareProvider;

    @BeforeEach
    void init() {
        auditorAwareProvider = new AuditorAwareProvider();
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void clear() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("인증_정보가_없으면_system_을_반환한다")
    void returnSystemIfNoauthentication() {
        // given
        SecurityContextHolder.clearContext();

        // when
        Optional<String> auditor = auditorAwareProvider.getCurrentAuditor();

        // then
        assertThat(auditor).contains(AuditorAwareProvider.SYSTEM);
    }

    @Test
    @DisplayName("anonymousUser_면_system_을_반환한다")
    void returnSystemIfAnonymousUser() {
        // given
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "anonymousUser",
                null,
                Collections.emptyList()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        Optional<String> auditor = auditorAwareProvider.getCurrentAuditor();

        // then
        assertThat(auditor).contains(AuditorAwareProvider.SYSTEM);
    }

    @Test
    @DisplayName("UserAuthentication_이면_USERGUID_를_반환한다")
    void returnUserEmailIfUserAuthentication() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                TEST_USER_GUID_1,
                TEST_EMAIL_1,
                UserRole.USER
        );

        UserAuthentication userAuthentication = new UserAuthentication(authenticatedUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userAuthentication, null, Collections.emptyList());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        Optional<String> auditor = auditorAwareProvider.getCurrentAuditor();

        // then
        assertThat(auditor).contains(TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("principal_이_AuthenticatedUser_면_USERGUID_를_반환한다")
    void returnEmailIfPrincipalIsAuthenticatedUser() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                TEST_USER_GUID_1,
                TEST_EMAIL_1,
                UserRole.USER
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedUser, null, Collections.emptyList());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        Optional<String> auditor = auditorAwareProvider.getCurrentAuditor();

        // then
        assertThat(auditor).contains(TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("알_수_없는_principal_이면_system_을_반환한다")
    void returnSystemIfPrincipalUnknown() {
        // given
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                new Object(),
                null,
                Collections.emptyList()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        Optional<String> auditor = auditorAwareProvider.getCurrentAuditor();

        // then
        assertThat(auditor).contains(AuditorAwareProvider.SYSTEM);
    }
}