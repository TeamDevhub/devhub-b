package teamdevhub.devhub.medium.adapter.out.common.provider.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.out.common.provider.jwt.JwtClaims;
import teamdevhub.devhub.adapter.out.common.provider.jwt.JwtTokenIssueProvider;
import teamdevhub.devhub.common.exception.AuthRuleException;

import java.lang.reflect.Field;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static teamdevhub.devhub.common.enums.ErrorCode.TOKEN_INVALID;
import static teamdevhub.devhub.domain.user.UserRole.USER;

class JwtTokenIssueProviderMediumTest {

    private JwtTokenIssueProvider jwtTokenIssueProvider;

    @BeforeEach
    void setUp() throws Exception {
        jwtTokenIssueProvider = new JwtTokenIssueProvider();

        String secret = "abcdefghijklmnopqrstuvwxyz123456";
        String base64Key = Base64.getEncoder().encodeToString(secret.getBytes());

        Field secretKeyField = JwtTokenIssueProvider.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        secretKeyField.set(jwtTokenIssueProvider, base64Key);

        jwtTokenIssueProvider.init();
    }

    @Test
    @DisplayName("accessToken_생성_시_Claims_가_올바르게_설정된다")
    void createAccessTokenTest() {
        String userGuid = "user-123";
        String email = "test@example.com";
        String userRole = "USER";

        String token = jwtTokenIssueProvider.createAccessToken(userGuid, email, USER);

        Claims claims = jwtTokenIssueProvider.parseClaims(token);
        assertThat(claims.getSubject()).isEqualTo(userGuid);
        assertThat(claims.get(JwtClaims.EMAIL, String.class)).isEqualTo(email);
        assertThat(claims.get(JwtClaims.USER_ROLE, String.class)).isEqualTo(userRole);
        assertThat(claims.get(JwtClaims.TOKEN_TYPE, String.class)).isEqualTo("ACCESS");
    }

    @Test
    @DisplayName("refreshToken_생성_및_userGuid_추출한다")
    void createRefreshTokenAndExtractUserGuidTest() {
        String userGuid = "user-456";

        String refreshToken = jwtTokenIssueProvider.createRefreshToken(userGuid);
        String extractedGuid = jwtTokenIssueProvider.extractUserGuidFromRefreshToken(refreshToken);

        assertThat(extractedGuid).isEqualTo(userGuid);
    }

    @Test
    @DisplayName("refreshToken_에_accessToken_을_넣으면_예외가_발생한다")
    void extractUserGuidWithAccessTokenThrows() {
        String token = jwtTokenIssueProvider.createAccessToken("user-789", "a@b.com", USER);

        assertThatThrownBy(() -> jwtTokenIssueProvider.extractUserGuidFromRefreshToken(token))
                .isInstanceOf(AuthRuleException.class)
                .hasMessageContaining(TOKEN_INVALID.getMessage());
    }

    @Test
    @DisplayName("resolveToken_과_removeBearer_로_Bearer_를_붙이거나_제거할_수_있다")
    void resolveAndRemoveBearerTest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String tokenValue = "abc123";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + tokenValue);

        String resolved = jwtTokenIssueProvider.resolveToken(request);
        String cleaned = jwtTokenIssueProvider.removeBearer(resolved);

        assertThat(resolved).isEqualTo("Bearer " + tokenValue);
        assertThat(cleaned).isEqualTo(tokenValue);
    }

    @Test
    @DisplayName("removeBearer_과정에서_잘못된_토큰이면_예외가_발생한다")
    void removeBearerInvalidTokenThrows() {
        assertThatThrownBy(() -> jwtTokenIssueProvider.removeBearer(null))
                .isInstanceOf(AuthRuleException.class)
                .hasMessageContaining(TOKEN_INVALID.getMessage());

        assertThatThrownBy(() -> jwtTokenIssueProvider.removeBearer("InvalidToken"))
                .isInstanceOf(AuthRuleException.class)
                .hasMessageContaining(TOKEN_INVALID.getMessage());
    }

    @Test
    @DisplayName("getPrefix_는_Bearer_를_반환한다")
    void getPrefixReturnsBearer() {
        assertThat(jwtTokenIssueProvider.getPrefix()).isEqualTo("Bearer ");
    }
}