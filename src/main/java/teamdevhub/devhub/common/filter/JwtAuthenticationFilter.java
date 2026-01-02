package teamdevhub.devhub.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import teamdevhub.devhub.adapter.in.common.vo.ApiDataResponseVo;
import teamdevhub.devhub.adapter.in.user.dto.request.LoginRequestDto;
import teamdevhub.devhub.adapter.out.common.util.JwtUtil;
import teamdevhub.devhub.common.auth.userdetails.UserDetailsImpl;
import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import teamdevhub.devhub.common.enums.SuccessCodeEnum;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.port.in.auth.RefreshTokenUseCase;
import teamdevhub.devhub.port.in.user.UserUseCase;
import teamdevhub.devhub.port.out.common.TokenProvider;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    private final TokenProvider tokenProvider;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final UserUseCase userUseCase;

    @PostConstruct
    public void init() {
        setFilterProcessesUrl("/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            LoginRequestDto loginRequestDto =
                    objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    loginRequestDto.getEmail(),
                                    loginRequestDto.getPassword()));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {

        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();
        String email = userDetails.getUsername();
        UserRole userRole = userDetails.getUser().getUserRole();

        String accessToken = tokenProvider.createAccessToken(email, userRole);
        String refreshToken = tokenProvider.createRefreshToken(email);
        refreshTokenUseCase.issueRefreshToken(email, refreshToken);
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/auth/reissue")
                .maxAge(14 * 24 * 60 * 60)
                .build();

        ApiDataResponseVo<?> responseBody = ApiDataResponseVo.successWithData(SuccessCodeEnum.LOGIN_SUCCESS, Map.of("accessToken", accessToken));

        String jsonResponse = objectMapper.writeValueAsString(responseBody);

        response.setHeader(HttpHeaders.AUTHORIZATION, JwtUtil.BEARER_PREFIX + accessToken);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        response.getWriter().write(jsonResponse);
        response.setStatus(HttpServletResponse.SC_OK);

        userUseCase.updateLastLoginDate(userDetails.getUserGuid());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        ApiDataResponseVo<?> apiResponse = ApiDataResponseVo.failureWithoutData(ErrorCodeEnum.LOGIN_FAIL);
        String jsonResponse = objectMapper.writeValueAsString(apiResponse);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}