package teamdevhub.devhub.common.filter;

import teamdevhub.devhub.common.component.CustomFilterExceptionHandler;
import teamdevhub.devhub.common.component.JwtAuthenticationProvider;
import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import teamdevhub.devhub.common.enums.JwtStatusEnum;
import teamdevhub.devhub.common.enums.TokenTypeEnum;
import teamdevhub.devhub.common.util.JwtUtil;
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
import teamdevhub.devhub.port.out.common.TokenProvider;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
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
        TokenTypeEnum tokenTypeEnum = TokenTypeEnum.valueOf(claims.get("token_type", String.class));
        if (tokenTypeEnum != TokenTypeEnum.ACCESS) {
            customFilterExceptionHandler.handle(httpServletResponse, ErrorCodeEnum.TOKEN_INVALID);
            return;
        }
        setAuthentication(claims.getSubject());
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    public void setAuthentication(String email) {
        Authentication authentication = jwtAuthenticationProvider.authenticate(email);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}