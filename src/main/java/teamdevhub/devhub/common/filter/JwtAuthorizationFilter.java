package teamdevhub.devhub.common.filter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import teamdevhub.devhub.common.component.CustomFilterExceptionHandler;
import teamdevhub.devhub.common.exception.AuthRuleException;
import teamdevhub.devhub.adapter.out.common.util.JwtClaims;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.common.enums.TokenType;
import teamdevhub.devhub.common.exception.FilterRuleException;
import teamdevhub.devhub.domain.common.record.auth.AuthenticatedUser;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.port.out.provider.TokenProvider;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final CustomFilterExceptionHandler customFilterExceptionHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = tokenProvider.resolveToken(httpServletRequest);

            if (!StringUtils.hasText(token)) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }

            String pureToken = tokenProvider.removeBearer(token);
            Claims claims = tokenProvider.parseClaims(pureToken);

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
            throw FilterRuleException.of(ErrorCode.TOKEN_INVALID);
        }
    }

    private void setAuthentication(Claims claims) {
        String userGuid = claims.getSubject();
        String email = claims.get(JwtClaims.EMAIL, String.class);
        UserRole userRole = UserRole.valueOf(
                claims.get(JwtClaims.USER_ROLE, String.class)
        );

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