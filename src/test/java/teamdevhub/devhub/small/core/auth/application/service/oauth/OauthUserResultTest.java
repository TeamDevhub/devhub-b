package teamdevhub.devhub.small.core.auth.application.service.oauth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.auth.application.service.oauth.OauthUserResult;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.core.user.domain.vo.UserRole;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class OauthUserResultTest {

    @Test
    @DisplayName("success_팩토리_메서드로_로그인_가능한_OauthUserResult_를_생성할_수_있다")
    void success_createsLoginAvailableResult() {
        // given
        AuthenticatedUser authenticatedUser = AuthenticatedUser.of(TEST_USER_GUID_1, TEST_EMAIL_1, UserRole.USER);

        // when
        OauthUserResult result = OauthUserResult.success(authenticatedUser);

        // then
        assertThat(result.loginAvailable()).isTrue();
        assertThat(result.authenticatedUser()).isEqualTo(authenticatedUser);
    }

    @Test
    @DisplayName("requiresSignup_팩토리_메서드로_로그인_불가_OauthUserResult_를_생성할_수_있다")
    void requiresSignup_createsLoginUnavailableResult() {
        // when
        OauthUserResult result = OauthUserResult.requiresSignup();

        // then
        assertThat(result.loginAvailable()).isFalse();
        assertThat(result.authenticatedUser()).isNull();
    }

    @Test
    @DisplayName("loginAvailable_이_true_이면_requireAuthenticatedUser_로_사용자를_가져올_수_있다")
    void requireAuthenticatedUser_whenLoginAvailable_returnsUser() {
        // given
        AuthenticatedUser authenticatedUser = AuthenticatedUser.of(TEST_USER_GUID_1, TEST_EMAIL_1, UserRole.USER);
        OauthUserResult result = OauthUserResult.success(authenticatedUser);

        // when
        AuthenticatedUser returned = result.requireAuthenticatedUser();

        // then
        assertThat(returned).isEqualTo(authenticatedUser);
    }

    @Test
    @DisplayName("loginAvailable_이_false_이면_requireAuthenticatedUser_호출_시_예외가_발생한다")
    void requireAuthenticatedUser_whenLoginUnavailable_throwsException() {
        // given
        OauthUserResult result = OauthUserResult.requiresSignup();

        // when, then
        assertThatThrownBy(result::requireAuthenticatedUser)
                .isInstanceOf(BusinessRuleException.class);
    }
}
