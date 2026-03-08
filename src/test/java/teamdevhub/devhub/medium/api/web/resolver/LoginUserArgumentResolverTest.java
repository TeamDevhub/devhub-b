package teamdevhub.devhub.medium.api.web.resolver;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import teamdevhub.devhub.api.web.resolver.LoginUser;
import teamdevhub.devhub.api.web.resolver.LoginUserArgumentResolver;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.outbound.common.exception.AuthRuleException;
import teamdevhub.devhub.outbound.security.auth.UserAuthentication;
import teamdevhub.devhub.outbound.auth.infrastructure.security.vo.AuthenticatedUser;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.fake.framework.FakeAuthentication;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class LoginUserArgumentResolverTest {

    private LoginUserArgumentResolver loginUserArgumentResolver;


    static class TestController {
        public void testMethod(@LoginUser AuthenticatedUser authenticatedUser) {}
        public void noAnnotationMethod(AuthenticatedUser authenticatedUser) {}
        public void wrongTypeMethod(@LoginUser String user) {}
    }

    @BeforeEach
    void init() {
        loginUserArgumentResolver = new LoginUserArgumentResolver();
    }

    @AfterEach
    void clear() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("supportsParameter_는_LoginUser_어노테이션과_AuthenticatedUser_타입을_지원한다")
    void supportsParameter_returnsTrueForLoginUserAnnotatedAuthenticatedUser() throws NoSuchMethodException {
        // given
        Method method = TestController.class.getMethod("testMethod", AuthenticatedUser.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);

        // when
        boolean result = loginUserArgumentResolver.supportsParameter(methodParameter);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("supportsParameter_는_어노테이션이_없으면_false_를_반환한다")
    void supportsParameter_returnsFalseWithoutAnnotation() throws NoSuchMethodException {
        // given
        Method method = TestController.class.getMethod("noAnnotationMethod", AuthenticatedUser.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);

        // when
        boolean result = loginUserArgumentResolver.supportsParameter(methodParameter);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("supportsParameter_는_타입이_다르면_false_를_반환한다")
    void supportsParameter_returnsFalseForWrongType() throws NoSuchMethodException {
        // given
        Method method = TestController.class.getMethod("wrongTypeMethod", String.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);

        // when
        boolean result = loginUserArgumentResolver.supportsParameter(methodParameter);

        // then
        assertThat(result).isFalse();
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