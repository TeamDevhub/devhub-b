package teamdevhub.devhub.infrastructure.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.shared.enums.TokenType;
import teamdevhub.devhub.shared.exception.AuthRuleException;
import teamdevhub.devhub.core.auth.domain.vo.token.AccessTokenInfo;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.common.provider.TokenParseProvider;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final TokenParseProvider tokenParseProvider;
    private final CustomFilterExceptionHandler customFilterExceptionHandler;
    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = httpServletRequest.getHeader(AUTHORIZATION_HEADER);

            if (!StringUtils.hasText(token)) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }

            String pureToken = tokenParseProvider.removeBearer(token);
            AccessTokenInfo accessTokenInfo = tokenParseProvider.getAccessTokenInfo(pureToken);
            validateAccessToken(accessTokenInfo);
            setAuthentication(accessTokenInfo);

            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (AuthRuleException authRuleException) {
            customFilterExceptionHandler.handle(httpServletResponse, authRuleException.getErrorCode());
        }
    }

    private void validateAccessToken(AccessTokenInfo accessTokenInfo) {
        if (accessTokenInfo.tokentype() != TokenType.ACCESS) {
            throw AuthRuleException.of(ErrorCode.TOKEN_INVALID);
        }
    }

    private void setAuthentication(AccessTokenInfo accessTokenInfo) {
        AuthenticatedUser authenticatedUser = AuthenticatedUser.of(
                accessTokenInfo.userGuid(),
                accessTokenInfo.email(),
                null,
                accessTokenInfo.userRole()
        );
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(accessTokenInfo.userRole().getAuthority()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedUser, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}