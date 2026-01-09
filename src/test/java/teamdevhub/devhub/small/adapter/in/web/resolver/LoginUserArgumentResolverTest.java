package teamdevhub.devhub.small.adapter.in.web.resolver;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import teamdevhub.devhub.adapter.in.web.resolver.LoginUserArgumentResolver;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.common.exception.AuthRuleException;
import teamdevhub.devhub.common.web.security.auth.UserAuthentication;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.vo.auth.AuthenticatedUser;
import teamdevhub.devhub.small.mock.infrastructure.FakeAuthentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.small.mock.constant.TestConstant.*;

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
    void UserAuthentication_principal_이면_AuthenticatedUser_를_반환한다() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, UserRole.USER);
        UserAuthentication userAuthentication = new UserAuthentication(authenticatedUser);
        SecurityContextHolder.getContext().setAuthentication(new FakeAuthentication(userAuthentication));

        // when
        Object resolvedValue = loginUserArgumentResolver.resolveArgument(null, null, null, null);

        // then
        assertThat(resolvedValue).isNotNull();
        assertThat(resolvedValue).isInstanceOf(AuthenticatedUser.class);
        assertThat(((AuthenticatedUser) resolvedValue).email()).isEqualTo(TEST_EMAIL);
    }

    @Test
    void AuthenticatedUser_principal_이면_그대로_반환한다() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, UserRole.USER);
        SecurityContextHolder.getContext().setAuthentication(new FakeAuthentication(authenticatedUser));

        // when
        Object resolved = loginUserArgumentResolver.resolveArgument(null, null, null, null);

        // then
        assertThat(resolved).isEqualTo(authenticatedUser);
    }

    @Test
    void principal_이_없으면_예외가_발생한다() {
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
    void principal_의_타입_틀리면_예외가_발생한다() {
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