package teamdevhub.devhub.medium.api.web.resolver;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import teamdevhub.devhub.api.web.resolver.LoginUser;
import teamdevhub.devhub.api.web.resolver.LoginUserArgumentResolver;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.fake.framework.FakeAuthentication;
import teamdevhub.devhub.outbound.common.exception.AuthRuleException;
import teamdevhub.devhub.outbound.security.auth.UserAuthentication;
import teamdevhub.devhub.shared.enums.ErrorCode;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_EMAIL_1;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_USER_GUID_1;

class LoginUserArgumentResolverTest {

    private LoginUserArgumentResolver loginUserArgumentResolver;

    static class TestController {

        public void testMethod(@LoginUser AuthenticatedUser authenticatedUser) {
        }

        public void optionalMethod(@LoginUser(required = false) AuthenticatedUser authenticatedUser) {
        }

        public void noAnnotationMethod(AuthenticatedUser authenticatedUser) {
        }

        public void wrongTypeMethod(@LoginUser String user) {
        }
    }

    @BeforeEach
    void init() {
        loginUserArgumentResolver = new LoginUserArgumentResolver();
    }

    @AfterEach
    void clear() {
        SecurityContextHolder.clearContext();
    }

    private MethodParameter loginUserParameter() throws NoSuchMethodException {
        Method method = TestController.class.getMethod("testMethod", AuthenticatedUser.class);
        return new MethodParameter(method, 0);
    }

    private MethodParameter optionalLoginUserParameter() throws NoSuchMethodException {
        Method method = TestController.class.getMethod("optionalMethod", AuthenticatedUser.class);
        return new MethodParameter(method, 0);
    }

    @Test
    @DisplayName("supportsParameter_는_LoginUser_어노테이션과_AuthenticatedUser_타입을_지원한다")
    void supportsParameter_returnsTrueForLoginUserAnnotatedAuthenticatedUser() throws Exception {
        // given
        MethodParameter parameter = loginUserParameter();

        // when
        boolean result = loginUserArgumentResolver.supportsParameter(parameter);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("supportsParameter_는_어노테이션이_없으면_false_를_반환한다")
    void supportsParameter_returnsFalseWithoutAnnotation() throws Exception {
        // given
        Method method = TestController.class.getMethod("noAnnotationMethod", AuthenticatedUser.class);
        MethodParameter parameter = new MethodParameter(method, 0);

        // when
        boolean result = loginUserArgumentResolver.supportsParameter(parameter);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("supportsParameter_는_타입이_다르면_false_를_반환한다")
    void supportsParameter_returnsFalseForWrongType() throws Exception {
        // given
        Method method = TestController.class.getMethod("wrongTypeMethod", String.class);
        MethodParameter parameter = new MethodParameter(method, 0);

        // when
        boolean result = loginUserArgumentResolver.supportsParameter(parameter);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("UserAuthentication_principal_이면_AuthenticatedUser_를_반환한다")
    void returnAuthenticatedUserIfUserAuthenticationPrincipal() throws Exception {
        // given
        AuthenticatedUser authenticatedUser =
                new AuthenticatedUser(TEST_USER_GUID_1, TEST_EMAIL_1, UserRole.USER);

        UserAuthentication userAuthentication = new UserAuthentication(authenticatedUser);

        SecurityContextHolder.getContext()
                .setAuthentication(new FakeAuthentication(userAuthentication));

        // when
        Object resolved =
                loginUserArgumentResolver.resolveArgument(loginUserParameter(), null, null, null);

        // then
        assertThat(resolved).isInstanceOf(AuthenticatedUser.class);
        assertThat(resolved).isEqualTo(authenticatedUser);
    }

    @Test
    @DisplayName("AuthenticatedUser_principal_이면_그대로_반환한다")
    void returnAuthenticatedUserIfPrincipalIsAlreadyAuthenticatedUser() throws Exception {
        // given
        AuthenticatedUser authenticatedUser =
                new AuthenticatedUser(TEST_USER_GUID_1, TEST_EMAIL_1, UserRole.USER);

        SecurityContextHolder.getContext()
                .setAuthentication(new FakeAuthentication(authenticatedUser));

        // when
        Object resolved =
                loginUserArgumentResolver.resolveArgument(loginUserParameter(), null, null, null);

        // then
        assertThat(resolved).isSameAs(authenticatedUser);
    }

    @Test
    @DisplayName("authentication이_없으면_예외가_발생한다")
    void throwIfAuthenticationMissing() throws Exception {
        // given
        SecurityContextHolder.clearContext();

        // then
        assertThatThrownBy(() ->
                loginUserArgumentResolver.resolveArgument(loginUserParameter(), null, null, null))
                .isInstanceOf(AuthRuleException.class)
                .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("principal이_null이면_예외가_발생한다")
    void throwIfPrincipalIsNull() throws Exception {
        // given
        SecurityContextHolder.getContext()
                .setAuthentication(new FakeAuthentication(null));

        // then
        assertThatThrownBy(() ->
                loginUserArgumentResolver.resolveArgument(loginUserParameter(), null, null, null))
                .isInstanceOf(AuthRuleException.class)
                .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("principal_타입이_다르면_예외가_발생한다")
    void throwIfPrincipalTypeMismatch() throws Exception {
        // given
        SecurityContextHolder.getContext()
                .setAuthentication(new FakeAuthentication("invalid-principal"));

        // then
        assertThatThrownBy(() ->
                loginUserArgumentResolver.resolveArgument(loginUserParameter(), null, null, null))
                .isInstanceOf(AuthRuleException.class)
                .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("required_false이고_authentication이_없으면_null을_반환한다")
    void returnNullIfOptionalAndAuthenticationMissing() throws Exception {
        // given
        SecurityContextHolder.clearContext();

        // when
        Object resolved =
                loginUserArgumentResolver.resolveArgument(optionalLoginUserParameter(), null, null, null);

        // then
        assertThat(resolved).isNull();
    }

    @Test
    @DisplayName("required_false이고_principal이_null이면_null을_반환한다")
    void returnNullIfOptionalAndPrincipalNull() throws Exception {
        // given
        SecurityContextHolder.getContext()
                .setAuthentication(new FakeAuthentication(null));

        // when
        Object resolved =
                loginUserArgumentResolver.resolveArgument(optionalLoginUserParameter(), null, null, null);

        // then
        assertThat(resolved).isNull();
    }

    @Test
    @DisplayName("required_false이고_principal_타입이_다르면_null을_반환한다")
    void returnNullIfOptionalAndPrincipalTypeMismatch() throws Exception {
        // given
        SecurityContextHolder.getContext()
                .setAuthentication(new FakeAuthentication("invalid-principal"));

        // when
        Object resolved =
                loginUserArgumentResolver.resolveArgument(optionalLoginUserParameter(), null, null, null);

        // then
        assertThat(resolved).isNull();
    }
}