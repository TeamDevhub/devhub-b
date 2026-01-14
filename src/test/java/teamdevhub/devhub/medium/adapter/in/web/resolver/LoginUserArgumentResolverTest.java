package teamdevhub.devhub.medium.adapter.in.web.resolver;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import teamdevhub.devhub.adapter.in.web.resolver.LoginUserArgumentResolver;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.common.exception.AuthRuleException;
import teamdevhub.devhub.common.web.security.auth.UserAuthentication;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.vo.auth.AuthenticatedUser;
import teamdevhub.devhub.fake.spring.infrastructure.FakeAuthentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class LoginUserArgumentResolverTest {

    private LoginUserArgumentResolver loginUserArgumentResolver;

    @BeforeEach
    void init() {
        loginUserArgumentResolver = new LoginUserArgumentResolver();
    }

    @AfterEach
    void clear() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("UserAuthentication_principal_이면_AuthenticatedUser_를_반환한다")
    void returnAuthenticatedUserIfUserAuthenticationPrincipal() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, UserRole.USER);
        UserAuthentication userAuthentication = new UserAuthentication(authenticatedUser);
        SecurityContextHolder.getContext().setAuthentication(new FakeAuthentication(userAuthentication));

        // when
        Object resolvedValue = loginUserArgumentResolver.resolveArgument(null, null, null, null);

        // then
        assertThat(resolvedValue).isNotNull();
        assertThat(resolvedValue).isInstanceOf(AuthenticatedUser.class);
        assertThat(((AuthenticatedUser) resolvedValue).email()).isEqualTo(TEST_EMAIL_1);
    }

    @Test
    @DisplayName("AuthenticatedUser_principal_이면_그대로_반환한다")
    void returnAuthenticatedUserIfPrincipalIsAlreadyAuthenticatedUser() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, UserRole.USER);
        SecurityContextHolder.getContext().setAuthentication(new FakeAuthentication(authenticatedUser));

        // when
        Object resolved = loginUserArgumentResolver.resolveArgument(null, null, null, null);

        // then
        assertThat(resolved).isEqualTo(authenticatedUser);
    }

    @Test
    @DisplayName("principal_이_없으면_예외가_발생한다")
    void throwIfPrincipalMissing() {
        // given
        SecurityContextHolder.getContext().setAuthentication(null);

        // then
        assertThatThrownBy(
                // when
                () -> loginUserArgumentResolver.resolveArgument(null, null, null, null))
                .isInstanceOf(AuthRuleException.class)
                .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("principal_의_타입_틀리면_예외가_발생한다")
    void throwIfPrincipalTypeMismatch() {
        // given
        SecurityContextHolder.getContext().setAuthentication(new FakeAuthentication("invalid-principal"));

        // then
        assertThatThrownBy(
                // when
                () -> loginUserArgumentResolver.resolveArgument(null, null, null, null))
                .isInstanceOf(AuthRuleException.class)
                .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());
    }
}