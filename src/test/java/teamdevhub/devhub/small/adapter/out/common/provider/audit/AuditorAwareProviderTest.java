package teamdevhub.devhub.small.adapter.out.common.provider.audit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import teamdevhub.devhub.adapter.out.common.provider.audit.AuditorAwareProvider;
import teamdevhub.devhub.common.web.security.auth.UserAuthentication;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.vo.auth.AuthenticatedUser;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.small.mock.constant.TestConstant.*;

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
    void 인증_정보가_없으면_system_을_반환한다() {
        // given
        SecurityContextHolder.clearContext();

        // when
        Optional<String> auditor = auditorAwareProvider.getCurrentAuditor();

        // then
        assertThat(auditor).contains(AuditorAwareProvider.SYSTEM);
    }

    @Test
    void anonymousUser_면_system_을_반환한다() {
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
    void UserAuthentication_이면_유저_이메일을_반환한다() {
        // given
        AuthenticatedUser authenticatedUser =
                new AuthenticatedUser(
                        TEST_GUID,
                        TEST_EMAIL,
                        TEST_PASSWORD,
                        UserRole.USER
                );

        UserAuthentication userAuthentication = new UserAuthentication(authenticatedUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userAuthentication,
                null,
                Collections.emptyList()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        Optional<String> auditor = auditorAwareProvider.getCurrentAuditor();

        // then
        assertThat(auditor).contains(TEST_EMAIL);
    }

    @Test
    void principal_이_AuthenticatedUser_면_이메일을_반환한다() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                TEST_GUID,
                TEST_EMAIL,
                TEST_PASSWORD,
                UserRole.USER
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authenticatedUser,
                null,
                Collections.emptyList()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        Optional<String> auditor = auditorAwareProvider.getCurrentAuditor();

        // then
        assertThat(auditor).contains(TEST_EMAIL);
    }

    @Test
    void 알_수_없는_principal_이면_system_을_반환한다() {
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