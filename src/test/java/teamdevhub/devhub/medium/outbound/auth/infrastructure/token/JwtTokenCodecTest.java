package teamdevhub.devhub.medium.outbound.auth.infrastructure.token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.outbound.auth.infrastructure.token.JwtTokenCodec;
import teamdevhub.devhub.shared.enums.VerificationProvider;
import teamdevhub.devhub.outbound.common.exception.AuthRuleException;
import teamdevhub.devhub.outbound.auth.infrastructure.token.vo.AccessTokenInfo;
import teamdevhub.devhub.outbound.auth.infrastructure.token.vo.TempTokenInfo;
import teamdevhub.devhub.fake.pure.application.provider.FakeTimeProvider;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;
import static teamdevhub.devhub.shared.enums.ErrorCode.TOKEN_EXPIRED;
import static teamdevhub.devhub.shared.enums.ErrorCode.TOKEN_INVALID;
import static teamdevhub.devhub.core.user.domain.vo.UserRole.USER;

class JwtTokenCodecTest {

    private JwtTokenCodec jwtTokenCodec;

    @BeforeEach
    void init() throws Exception {
        FakeTimeProvider fakeTimeProvider = new FakeTimeProvider(LocalDateTime.now());
        jwtTokenCodec = new JwtTokenCodec(fakeTimeProvider);

        String secret = "abcdefghijklmnopqrstuvwxyz123456";
        String base64Key = Base64.getEncoder().encodeToString(secret.getBytes());

        Field secretKeyField = JwtTokenCodec.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        secretKeyField.set(jwtTokenCodec, base64Key);

        jwtTokenCodec.init();
    }

    @Test
    @DisplayName("accessToken_생성_후_토큰_정보를_정상적으로_추출한다")
    void createAccessTokenAndExtractInfo() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(TEST_USER_GUID_1, TEST_EMAIL_1, UserRole.USER);

        // when
        String accessToken = jwtTokenCodec.createAccessToken(authenticatedUser);
        AccessTokenInfo accessTokenInfo = jwtTokenCodec.getAccessTokenInfo(accessToken);

        // then
        assertThat(accessTokenInfo.userGuid()).isEqualTo(authenticatedUser.userGuid());
        assertThat(accessTokenInfo.email()).isEqualTo(TEST_EMAIL_1);
        assertThat(accessTokenInfo.userRole()).isEqualTo(USER);
    }

    @Test
    @DisplayName("refreshToken_을_accessToken_parser_에_넣으면_TOKEN_INVALID_예외가_발생한다")
    void extractAccessTokenInfoWithRefreshTokenThrows() {
        // given
        String refreshToken = jwtTokenCodec.createRefreshToken(TEST_USER_GUID_1);

        // when, then
        assertThatThrownBy(() ->
                jwtTokenCodec.getAccessTokenInfo(refreshToken))
                .isInstanceOf(AuthRuleException.class)
                .hasMessageContaining(TOKEN_INVALID.getMessage());
    }

    @Test
    @DisplayName("refreshToken_생성_후_userGuid_를_정상_추출할_수_있다")
    void createRefreshTokenAndExtractUserGuid() {
        // given
        String userGuid = TEST_USER_GUID_1;

        // when
        String refreshToken = jwtTokenCodec.createRefreshToken(userGuid);
        String extracted = jwtTokenCodec.getRefreshTokenInfo(refreshToken);

        // then
        assertThat(extracted).isEqualTo(userGuid);
    }

    @Test
    @DisplayName("accessToken_을_refreshToken_parser_에_넣으면_TOKEN_INVALID_예외가_발생한다")
    void extractRefreshTokenInfoWithAccessTokenThrows() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(TEST_USER_GUID_1, TEST_EMAIL_1, UserRole.USER);
        String accessToken = jwtTokenCodec.createAccessToken(authenticatedUser);

        // when, then
        assertThatThrownBy(() ->
                jwtTokenCodec.getRefreshTokenInfo(accessToken))
                .isInstanceOf(AuthRuleException.class)
                .hasMessageContaining(TOKEN_INVALID.getMessage());
    }

    @Test
    @DisplayName("tempToken_생성_후_토큰_정보가_정상_추출된다")
    void createTempTokenAndExtractInfo() {
        // given
        String oauthId = "oauth-id-123";

        // when
        String tempToken = jwtTokenCodec.createTempToken(
                oauthId,
                VerificationProvider.GOOGLE,
                TEST_EMAIL_1
        );
        TempTokenInfo tempTokenInfo = jwtTokenCodec.getTempTokenInfo(tempToken);

        // then
        assertThat(tempTokenInfo.oauthId()).isEqualTo(oauthId);
        assertThat(tempTokenInfo.verificationProvider()).isEqualTo(VerificationProvider.GOOGLE);
        assertThat(tempTokenInfo.email()).isEqualTo(TEST_EMAIL_1);
    }

    @Test
    @DisplayName("accessToken_을_tempToken_parser_에_넣으면_TOKEN_INVALID_예외가_발생한다")
    void extractTempTokenInfoWithAccessTokenThrows() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(TEST_USER_GUID_1, TEST_EMAIL_1, UserRole.USER);
        String accessToken = jwtTokenCodec.createAccessToken(authenticatedUser);


        // when, then
        assertThatThrownBy(() ->
                jwtTokenCodec.getTempTokenInfo(accessToken))
                .isInstanceOf(AuthRuleException.class)
                .hasMessageContaining(TOKEN_INVALID.getMessage());
    }

    @Test
    @DisplayName("Bearer_prefix_를_정상적으로_제거한다")
    void removeBearer_success() {
        // given
        String tokenWithBearer = "Bearer abc.def.ghi";

        // when
        String result = jwtTokenCodec.removeBearer(tokenWithBearer);

        // then
        assertThat(result).isEqualTo("abc.def.ghi");
    }

    @Test
    @DisplayName("Bearer_prefix_없으면_TOKEN_INVALID_예외가_발생한다")
    void removeBearer_withoutPrefixThrows() {
        // when, then
        assertThatThrownBy(() ->
                jwtTokenCodec.removeBearer("abc.def.ghi"))
                .isInstanceOf(AuthRuleException.class)
                .hasMessageContaining(TOKEN_INVALID.getMessage());
    }

    @Test
    @DisplayName("Bearer_null_입력_시_TOKEN_INVALID_예외가_발생한다")
    void removeBearer_nullThrows() {
        // when, then
        assertThatThrownBy(() ->
                jwtTokenCodec.removeBearer(null))
                .isInstanceOf(AuthRuleException.class)
                .hasMessageContaining(TOKEN_INVALID.getMessage());
    }

    @Test
    @DisplayName("만료된_accessToken_은_TOKEN_EXPIRED_예외가_발생한다")
    void expiredAccessTokenThrowsAgain() throws NoSuchFieldException, IllegalAccessException {
        // given
        FakeTimeProvider fakeTimeProvider = new FakeTimeProvider(LocalDateTime.now().minusHours(2));
        jwtTokenCodec = new JwtTokenCodec(fakeTimeProvider);
        String secret = "abcdefghijklmnopqrstuvwxyz123456";
        String base64Key = Base64.getEncoder().encodeToString(secret.getBytes());

        Field secretKeyField = JwtTokenCodec.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        secretKeyField.set(jwtTokenCodec, base64Key);

        jwtTokenCodec.init();

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(TEST_USER_GUID_1, TEST_EMAIL_1, UserRole.USER);
        String accessToken = jwtTokenCodec.createAccessToken(authenticatedUser);
        fakeTimeProvider.setNow(LocalDateTime.now());

        // when, then
        assertThatThrownBy(() ->
                jwtTokenCodec.getAccessTokenInfo(accessToken))
                .isInstanceOf(AuthRuleException.class)
                .hasMessageContaining(TOKEN_EXPIRED.getMessage());
    }
}