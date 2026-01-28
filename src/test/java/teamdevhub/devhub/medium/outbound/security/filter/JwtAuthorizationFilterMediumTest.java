package teamdevhub.devhub.medium.outbound.security.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import teamdevhub.devhub.core.auth.domain.vo.token.AccessTokenInfo;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.fake.framework.FakeCustomFilterExceptionHandler;
import teamdevhub.devhub.fake.pure.application.provider.FakeTokenParseProvider;
import teamdevhub.devhub.outbound.security.filter.JwtAuthorizationFilter;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.shared.exception.AuthRuleException;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_EMAIL_1;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_USER_GUID_1;

class JwtAuthorizationFilterMediumTest {

    private JwtAuthorizationFilter jwtAuthorizationFilter;

    private FakeTokenParseProvider tokenParseProvider;
    private FakeCustomFilterExceptionHandler customFilterExceptionHandler;

    private MockHttpServletRequest httpServletRequest;
    private MockHttpServletResponse httpServletResponse;
    private MockFilterChain filterChain;

    @BeforeEach
    void init() {
        tokenParseProvider = new FakeTokenParseProvider();
        customFilterExceptionHandler = new FakeCustomFilterExceptionHandler();
        jwtAuthorizationFilter = new JwtAuthorizationFilter(tokenParseProvider, customFilterExceptionHandler);

        httpServletRequest = new MockHttpServletRequest();
        httpServletResponse = new MockHttpServletResponse();
        filterChain = new MockFilterChain();

        SecurityContextHolder.clearContext();
    }

    private AccessTokenInfo makeAccessTokenInfo() {
        return new AccessTokenInfo(
                TEST_USER_GUID_1,
                TEST_EMAIL_1,
                UserRole.USER
        );
    }

    @Test
    @DisplayName("토큰이_없으면_다음_필터를_실행한다")
    void proceed_to_next_filter_if_no_token() throws Exception {
        // given

        // when
        jwtAuthorizationFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);

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
        jwtAuthorizationFilter = new JwtAuthorizationFilter(tokenParseProvider, customFilterExceptionHandler);

        // when
        jwtAuthorizationFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);

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
        jwtAuthorizationFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);

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
