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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import teamdevhub.devhub.adapter.out.common.provider.jwt.JwtClaims;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.common.enums.TokenType;
import teamdevhub.devhub.common.exception.AuthRuleException;
import teamdevhub.devhub.common.web.security.filter.CustomFilterExceptionHandler;
import teamdevhub.devhub.common.web.security.filter.JwtAuthorizationFilter;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.user.vo.AuthenticatedUser;
import teamdevhub.devhub.port.out.auth.TokenParseProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_EMAIL_1;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_USER_GUID_1;

@ExtendWith(MockitoExtension.class)
class JwtAuthorizationFilterMediumTest {

    @Mock
    private TokenParseProvider tokenParseProvider;

    @Mock
    private CustomFilterExceptionHandler customFilterExceptionHandler;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Mock
    private FilterChain filterChain;

    private JwtAuthorizationFilter filter;

    @BeforeEach
    void init() {
        filter = new JwtAuthorizationFilter(tokenParseProvider, customFilterExceptionHandler);
        SecurityContextHolder.clearContext();
    }

    private Claims makeClaims() {
        Claims claims = Jwts.claims();
        claims.setSubject(teamdevhub.devhub.constant.UserTestConstant.TEST_USER_GUID_1);
        claims.put(JwtClaims.TOKEN_TYPE, TokenType.ACCESS.name());
        claims.put(JwtClaims.EMAIL, teamdevhub.devhub.constant.UserTestConstant.TEST_EMAIL_1);
        claims.put(JwtClaims.USER_ROLE, UserRole.USER.name());
        return claims;
    }

    @Test
    @DisplayName("토큰이_없으면_다음_필터를_실행한다")
    void proceedToNextFilterIfNoToken() throws Exception {
        when(tokenParseProvider.resolveToken(httpServletRequest)).thenReturn(null);

        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        verify(filterChain).doFilter(httpServletRequest, httpServletResponse);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    @DisplayName("유효하지_않은_토큰이면_CustomFilterExceptionHandler_를_호출한다")
    void callErrorHandlerIfTokenInvalid() throws Exception {
        String token = "Bearer invalid";
        when(tokenParseProvider.resolveToken(httpServletRequest)).thenReturn(token);
        when(tokenParseProvider.removeBearer(token)).thenReturn("invalidToken");
        when(tokenParseProvider.parseClaims("invalidToken"))
                .thenThrow(AuthRuleException.of(ErrorCode.TOKEN_INVALID));

        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        verify(customFilterExceptionHandler).handle(eq(httpServletResponse), eq(ErrorCode.TOKEN_INVALID));
        verify(filterChain, never()).doFilter(httpServletRequest, httpServletResponse);
    }

    @Test
    @DisplayName("액세스_토큰이_아닌_경우_TOKEN_INVALID_예외를_처리한다")
    void rejectIfTokenTypeIsNotAccess() throws Exception {
        // given
        String token = "Bearer valid-refresh-token";
        Claims claims = makeClaims();
        claims.put(JwtClaims.TOKEN_TYPE, TokenType.REFRESH.name()); // 핵심 포인트

        when(tokenParseProvider.resolveToken(httpServletRequest)).thenReturn(token);
        when(tokenParseProvider.removeBearer(token)).thenReturn("refreshToken");
        when(tokenParseProvider.parseClaims("refreshToken")).thenReturn(claims);

        // when
        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        // then
        verify(customFilterExceptionHandler).handle(eq(httpServletResponse), eq(ErrorCode.TOKEN_INVALID));
        verify(filterChain, never()).doFilter(httpServletRequest, httpServletResponse);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    @DisplayName("유효한_액세스_토큰이면_Authentication_을_설정하고_다음_필터를_실행한다")
    void setAuthenticationAndProceedIfAccessTokenValid() throws Exception {
        String token = "Bearer valid";
        Claims claims = makeClaims();

        when(tokenParseProvider.resolveToken(httpServletRequest)).thenReturn(token);
        when(tokenParseProvider.removeBearer(token)).thenReturn("validToken");
        when(tokenParseProvider.parseClaims("validToken")).thenReturn(claims);

        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        verify(filterChain).doFilter(httpServletRequest, httpServletResponse);

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
