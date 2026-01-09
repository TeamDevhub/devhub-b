package teamdevhub.devhub.small.common.web.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import teamdevhub.devhub.common.web.security.handler.CustomAccessDeniedHandler;
import teamdevhub.devhub.small.mock.web.FakeHttpServletRequest;
import teamdevhub.devhub.small.mock.web.FakeHttpServletResponse;

import java.io.StringWriter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CustomAccessDeniedHandlerTest {

    @Test
    void 접근거부_응답으로_JSON_을_반환한다() throws Exception {
        // given
        CustomAccessDeniedHandler customAccessDeniedHandler = new CustomAccessDeniedHandler();
        HttpServletRequest httpServletRequest = new FakeHttpServletRequest();

        StringWriter stringWriter = new StringWriter();
        HttpServletResponse httpServletResponse = new FakeHttpServletResponse(stringWriter);

        AccessDeniedException accessDeniedException = new AccessDeniedException("Access Denied");

        // when
        customAccessDeniedHandler.handle(httpServletRequest, httpServletResponse, accessDeniedException);

        // then
        assertThat(httpServletResponse.getStatus()).isEqualTo(403);
        assertThat(stringWriter.toString()).contains("Access Denied");
    }
}