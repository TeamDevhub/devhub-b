package teamdevhub.devhub.medium.common.web.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
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
import teamdevhub.devhub.domain.vo.auth.AuthenticatedUser;
import teamdevhub.devhub.port.out.auth.TokenParseProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static teamdevhub.devhub.medium.mock.constant.TestConstant.TEST_EMAIL;
import static teamdevhub.devhub.medium.mock.constant.TestConstant.TEST_GUID;

@ExtendWith(MockitoExtension.class)
class JwtAuthorizationFilterTest {

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

    private Claims makeClaims(String userGuid, String email, UserRole role) {
        Claims claims = Jwts.claims();
        claims.setSubject(userGuid);
        claims.put(JwtClaims.TOKEN_TYPE, TokenType.ACCESS.name());
        claims.put(JwtClaims.EMAIL, email);
        claims.put(JwtClaims.USER_ROLE, role.name());
        return claims;
    }

    @Test
    void 토큰이_없으면_다음_필터를_실행한다() throws Exception {
        when(tokenParseProvider.resolveToken(httpServletRequest)).thenReturn(null);

        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        verify(filterChain).doFilter(httpServletRequest, httpServletResponse);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void 유효하지_않은_토큰이면_에러핸들러를_호출한다() throws Exception {
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
    void 유효한_액세스_토큰이면_Authentication_을_설정하고_다음_필터를_실행한다() throws Exception {
        String token = "Bearer valid";
        Claims claims = makeClaims(TEST_GUID, TEST_EMAIL, UserRole.USER);

        when(tokenParseProvider.resolveToken(httpServletRequest)).thenReturn(token);
        when(tokenParseProvider.removeBearer(token)).thenReturn("validToken");
        when(tokenParseProvider.parseClaims("validToken")).thenReturn(claims);

        filter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        verify(filterChain).doFilter(httpServletRequest, httpServletResponse);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        assertThat(user.userGuid()).isEqualTo(TEST_GUID);
        assertThat(user.email()).isEqualTo(TEST_EMAIL);
        assertThat(authentication.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly(UserRole.USER.getAuthority());
    }
}
