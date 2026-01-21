package teamdevhub.devhub.medium.common.web.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import teamdevhub.devhub.adapter.out.infrastructure.token.JwtClaims;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.common.enums.SignupStatus;
import teamdevhub.devhub.common.enums.TokenType;
import teamdevhub.devhub.common.exception.AuthRuleException;
import teamdevhub.devhub.common.web.security.filter.CustomFilterExceptionHandler;
import teamdevhub.devhub.common.web.security.filter.JwtAuthorizationFilter;
import teamdevhub.devhub.domain.auth.vo.token.AccessTokenInfo;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.fake.pure.handler.FakeCustomFilterExceptionHandler;
import teamdevhub.devhub.fake.pure.provider.FakeTokenParseProvider;
import teamdevhub.devhub.port.out.provider.TokenParseProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_EMAIL_1;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_USER_GUID_1;

class JwtAuthorizationFilterMediumTest {

    private FakeTokenParseProvider tokenParseProvider;
    private FakeCustomFilterExceptionHandler customFilterExceptionHandler;
    private JwtAuthorizationFilter filter;

    private MockHttpServletRequest httpServletRequest;
    private MockHttpServletResponse httpServletResponse;
    private MockFilterChain filterChain;

    private static final String TEST_USER_GUID_1 = "user-guid-1";
    private static final String TEST_EMAIL_1 = "test@email.com";

    @BeforeEach
    void init() {
        tokenParseProvider = new FakeTokenParseProvider();
        customFilterExceptionHandler = new FakeCustomFilterExceptionHandler();
        filter = new JwtAuthorizationFilter(tokenParseProvider, customFilterExceptionHandler);

        httpServletRequest = new MockHttpServletRequest();
        httpServletResponse = new MockHttpServletResponse();
        filterChain = new MockFilterChain();

        SecurityContextHolder.clearContext();
    }

    private AccessTokenInfo makeAccessTokenInfo() {
        return new AccessTokenInfo(
                TEST_USER_GUID_1,
                TokenType.ACCESS,
                SignupStatus.COMPLETED,
                TEST_EMAIL_1,
                UserRole.USER
        );
    }

    @Test
    @DisplayName("토큰이_없으면_다음_필터를_실행한다")
    void proceed_to_next_filter_if_no_token() throws Exception {
        // given

        // when
        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        // then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        assertThat(filterChain.getRequest()).isNotNull(); // 필터 체인 실행됨
    }

    @Test
    @DisplayName("유효하지_않은_토큰이면_CustomFilterExceptionHandler_를_호출한다")
    void call_error_handler_if_token_invalid() throws Exception {
        // given
        String token = "Bearer invalid";
        httpServletRequest.addHeader("Authorization", token);
        tokenParseProvider = new FakeTokenParseProvider() {
            @Override
            public AccessTokenInfo getAccessTokenInfo(String accessToken) {
                throw AuthRuleException.of(ErrorCode.TOKEN_INVALID);
            }
        };
        filter = new JwtAuthorizationFilter(tokenParseProvider, customFilterExceptionHandler);

        // when
        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        // then
        assertThat(customFilterExceptionHandler.isHandled()).isTrue();
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    @DisplayName("액세스_토큰이_아닌_경우_TOKEN_INVALID_예외를_처리한다")
    void reject_if_token_type_is_not_access() throws Exception {
        // given
        String token = "Bearer refresh-token";
        httpServletRequest.addHeader("Authorization", token);

        AccessTokenInfo tokenInfo = new AccessTokenInfo(
                TEST_USER_GUID_1,
                TokenType.REFRESH,
                SignupStatus.COMPLETED,
                TEST_EMAIL_1,
                UserRole.USER
        );
        tokenParseProvider.givenAccessToken("refresh-token", tokenInfo);

        // when
        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        // then
        assertThat(customFilterExceptionHandler.isHandled()).isTrue();
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    @DisplayName("유효한_액세스_토큰이면_Authentication_을_설정하고_다음_필터를_실행한다")
    void set_authentication_and_proceed_if_access_token_valid() throws Exception {
        // given
        String token = "Bearer valid";
        httpServletRequest.addHeader("Authorization", token);

        AccessTokenInfo tokenInfo = makeAccessTokenInfo();
        tokenParseProvider.givenAccessToken("valid", tokenInfo);

        // when
        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        // then
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        assertThat(user.userGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(user.email()).isEqualTo(TEST_EMAIL_1);
        assertThat(authentication.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly(UserRole.USER.getAuthority());
    }
}
