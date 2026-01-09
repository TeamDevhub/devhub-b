package teamdevhub.devhub.small.common.web.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.AuthenticationException;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.common.web.security.handler.CustomAuthenticationEntryPoint;
import teamdevhub.devhub.small.mock.web.FakeHttpServletRequest;
import teamdevhub.devhub.small.mock.web.FakeHttpServletResponse;

import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomAuthenticationEntryPointTest {

    @Test
    void 인증실패_응답으로_JSON_을_반환한다() throws Exception {
        // given
        CustomAuthenticationEntryPoint customAuthenticationEntryPoint = new CustomAuthenticationEntryPoint();
        HttpServletRequest httpServletRequest = new FakeHttpServletRequest();

        StringWriter stringWriter = new StringWriter();
        HttpServletResponse httpServletResponse = new FakeHttpServletResponse(stringWriter);

        AuthenticationException exception = new AuthenticationException(ErrorCode.AUTH_INVALID.getMessage()) {};

        // when
        customAuthenticationEntryPoint.commence(httpServletRequest, httpServletResponse, exception);

        // then
        assertThat(httpServletResponse.getStatus()).isEqualTo(401);
        assertThat(stringWriter.toString()).contains(ErrorCode.AUTH_INVALID.getMessage());
    }
}
