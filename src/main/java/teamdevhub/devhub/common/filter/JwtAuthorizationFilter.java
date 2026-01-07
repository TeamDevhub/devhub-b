package teamdevhub.devhub.common.filter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import teamdevhub.devhub.adapter.in.common.component.CustomFilterExceptionHandler;
import teamdevhub.devhub.common.JwtClaims;
import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import teamdevhub.devhub.common.enums.JwtStatusEnum;
import teamdevhub.devhub.common.enums.TokenTypeEnum;
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

        String token = tokenProvider.getTokenFromHeader(httpServletRequest);

        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        String pureToken = tokenProvider.substringHeaderToken(token);
        JwtStatusEnum status = tokenProvider.validateToken(pureToken);

        if (status == JwtStatusEnum.EXPIRED) {
            customFilterExceptionHandler.handle(httpServletResponse, ErrorCodeEnum.TOKEN_EXPIRED);
            return;
        }

        if (status == JwtStatusEnum.INVALID) {
            customFilterExceptionHandler.handle(httpServletResponse, ErrorCodeEnum.TOKEN_INVALID);
            return;
        }

        Claims claims = tokenProvider.getUserInfo(pureToken);
        TokenTypeEnum tokenTypeEnum = TokenTypeEnum.valueOf(claims.get(TOKEN_TYPE, String.class));
        if (tokenTypeEnum != TokenTypeEnum.ACCESS) {
            customFilterExceptionHandler.handle(httpServletResponse, ErrorCodeEnum.TOKEN_INVALID);
            return;
        }
        setAuthentication(claims);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void setAuthentication(Claims claims) {
        String userGuid = claims.getSubject(); // sub = userGuid
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