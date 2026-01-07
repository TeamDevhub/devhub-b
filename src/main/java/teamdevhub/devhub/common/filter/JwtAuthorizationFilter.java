package teamdevhub.devhub.common.filter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import teamdevhub.devhub.adapter.in.common.component.CustomFilterExceptionHandler;
import teamdevhub.devhub.common.JwtClaims;
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
import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import teamdevhub.devhub.common.enums.TokenTypeEnum;
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

    private static final String TOKEN_TYPE = "token_type";
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

        } catch (FilterRuleException e) {
            customFilterExceptionHandler.handle(httpServletResponse, e.getErrorCodeEnum());
        }
    }

    private void validateAccessToken(Claims claims) {
        TokenTypeEnum type = TokenTypeEnum.valueOf(claims.get(JwtClaims.TOKEN_TYPE, String.class));

        if (type != TokenTypeEnum.ACCESS) {
            throw FilterRuleException.of(ErrorCodeEnum.TOKEN_INVALID);
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