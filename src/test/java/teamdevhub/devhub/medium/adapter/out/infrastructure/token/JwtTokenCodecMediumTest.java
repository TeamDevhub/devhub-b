package teamdevhub.devhub.medium.adapter.out.infrastructure.token;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.out.infrastructure.token.JwtClaims;
import teamdevhub.devhub.adapter.out.infrastructure.token.JwtTokenCodec;
import teamdevhub.devhub.common.enums.SignupStatus;
import teamdevhub.devhub.common.exception.AuthRuleException;

import java.lang.reflect.Field;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static teamdevhub.devhub.common.enums.ErrorCode.TOKEN_INVALID;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_EMAIL_1;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_USER_GUID_1;
import static teamdevhub.devhub.domain.user.UserRole.USER;

class JwtTokenCodecMediumTest {

    private JwtTokenCodec jwtTokenCodec;

    @BeforeEach
    void init() throws Exception {
        jwtTokenCodec = new JwtTokenCodec();

        String secret = "abcdefghijklmnopqrstuvwxyz123456";
        String base64Key = Base64.getEncoder().encodeToString(secret.getBytes());

        Field secretKeyField = JwtTokenCodec.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        secretKeyField.set(jwtTokenCodec, base64Key);

        jwtTokenCodec.init();
    }

    @Test
    @DisplayName("refreshToken_생성_및_userGuid_추출한다")
    void createRefreshTokenAndExtractUserGuidTest() {
        // given
        String userGuid = TEST_USER_GUID_1;

        // when
        String refreshToken = jwtTokenCodec.createRefreshToken(userGuid);
        String extractedGuid = jwtTokenCodec.getRefreshTokenInfo(refreshToken).userGuid();

        // then
        assertThat(extractedGuid).isEqualTo(userGuid);
    }

    @Test
    @DisplayName("refreshToken_에_accessToken_을_넣으면_예외가_발생한다")
    void extractUserGuidWithAccessTokenThrows() {
        // given
        String token = jwtTokenCodec.createAccessToken(TEST_USER_GUID_1, SignupStatus.COMPLETED, TEST_EMAIL_1, USER);

        // then
        assertThatThrownBy(
                // when
                () -> jwtTokenCodec.getRefreshTokenInfo(token))
                .isInstanceOf(AuthRuleException.class)
                .hasMessageContaining(TOKEN_INVALID.getMessage());
    }

    @Test
    @DisplayName("removeBearer_로_Bearer_를_제거할_수_있다")
    void removeBearer_strips_Bearer_prefix() {
        // given
        String tokenWithBearer = "Bearer abc123";

        // when
        String cleaned = jwtTokenCodec.removeBearer(tokenWithBearer);

        // then
        assertThat(cleaned).isEqualTo("abc123");
    }

    @Test
    @DisplayName("Bearer_가_없는_토큰이면_예외가_발생한다")
    void removeBearer_with_no_prefix_returns_same_token() {
        // given
        String token = "abc123";

        // when, then
        assertThatThrownBy(() -> jwtTokenCodec.removeBearer(token))
                .isInstanceOf(AuthRuleException.class)
                .hasMessageContaining(TOKEN_INVALID.getMessage());
    }

    @Test
    @DisplayName("removeBearer_null_입력_시_예외가_발생한다")
    void removeBearerNullThrows() {
        // given, when, then
        assertThatThrownBy(() -> jwtTokenCodec.removeBearer(null))
                .isInstanceOf(AuthRuleException.class)
                .hasMessageContaining(TOKEN_INVALID.getMessage());
    }

    @Test
    @DisplayName("removeBearer_과정에서_잘못된_토큰이면_예외가_발생한다")
    void removeBearerInvalidTokenThrows() {
        // then
        assertThatThrownBy(
                // given, when
                () -> jwtTokenCodec.removeBearer(null))
                .isInstanceOf(AuthRuleException.class)
                .hasMessageContaining(TOKEN_INVALID.getMessage());

        // then
        assertThatThrownBy(
                // given, when
                () -> jwtTokenCodec.removeBearer("InvalidToken"))
                .isInstanceOf(AuthRuleException.class)
                .hasMessageContaining(TOKEN_INVALID.getMessage());
    }
}