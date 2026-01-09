package teamdevhub.devhub.common.web.security.filter;

import io.jsonwebtoken.Claims;
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
import teamdevhub.devhub.adapter.out.common.provider.jwt.JwtClaims;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.common.enums.TokenType;
import teamdevhub.devhub.common.exception.AuthRuleException;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.vo.auth.AuthenticatedUser;
import teamdevhub.devhub.port.out.auth.TokenParseProvider;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final TokenParseProvider tokenParseProvider;
    private final CustomFilterExceptionHandler customFilterExceptionHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = tokenParseProvider.resolveToken(httpServletRequest);

            if (!StringUtils.hasText(token)) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }

            String pureToken = tokenParseProvider.removeBearer(token);
            Claims claims = tokenParseProvider.parseClaims(pureToken);
            validateAccessToken(claims);

            setAuthentication(claims);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (AuthRuleException e) {
            customFilterExceptionHandler.handle(httpServletResponse, e.getErrorCode());
        }
    }

    private void validateAccessToken(Claims claims) {
        TokenType type = TokenType.valueOf(claims.get(JwtClaims.TOKEN_TYPE, String.class));

        if (type != TokenType.ACCESS) {
            throw AuthRuleException.of(ErrorCode.TOKEN_INVALID);
        }
    }

    private void setAuthentication(Claims claims) {
        String userGuid = claims.getSubject();
        String email = claims.get(JwtClaims.EMAIL, String.class);
        UserRole userRole = UserRole.valueOf(claims.get(JwtClaims.USER_ROLE, String.class));

        AuthenticatedUser authenticatedUser =
                AuthenticatedUser.of(userGuid, email, null, userRole);

        Collection<? extends GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(userRole.getAuthority()));

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        authenticatedUser,
                        null,
                        authorities
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}