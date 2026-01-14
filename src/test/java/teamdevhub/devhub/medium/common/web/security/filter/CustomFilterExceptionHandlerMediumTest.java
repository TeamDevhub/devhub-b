package teamdevhub.devhub.medium.common.web.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import teamdevhub.devhub.adapter.in.web.dto.response.ApiDataResponseDto;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.common.web.security.filter.CustomFilterExceptionHandler;

import java.io.PrintWriter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class CustomFilterExceptionHandlerMediumTest {

    private CustomFilterExceptionHandler customFilterExceptionHandler;
    private MockHttpServletResponse mockHttpServletResponse;
    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        customFilterExceptionHandler = new CustomFilterExceptionHandler();
        mockHttpServletResponse = new MockHttpServletResponse();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("httpServletResponse_에_에러코드_응답을_JSON_으로_작성한다")
    void writeErrorResponseAsJson() throws Exception {
        // given
        ErrorCode errorCode = ErrorCode.TOKEN_UNSUPPORTED;

        // when
        customFilterExceptionHandler.handle(mockHttpServletResponse, errorCode);

        // then
        assertThat(mockHttpServletResponse.getStatus()).isEqualTo(errorCode.getStatus().value());
        assertThat(mockHttpServletResponse.getContentType()).isEqualTo("application/json;charset=UTF-8");
        assertThat(mockHttpServletResponse.getCharacterEncoding()).isEqualTo("UTF-8");
        String body = mockHttpServletResponse.getContentAsString();
        ApiDataResponseDto<?> result = objectMapper.readValue(body, ApiDataResponseDto.class);
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getError().getMessage()).isEqualTo(errorCode.getMessage());
    }

    @Test
    @DisplayName("응답_작성_중_예외가_발생해도_외부로_전파되지_않는다")
    void swallowExceptionIfResponseWriteFails(){
        // given
        ErrorCode errorCode = ErrorCode.TOKEN_INVALID;

        HttpServletResponse brokenResponse = new HttpServletResponseWrapper(mockHttpServletResponse) {
            @Override
            public PrintWriter getWriter() {
                throw new RuntimeException("Test Error occurred");
            }
        };

        // when, then
        assertThatCode(
                () -> customFilterExceptionHandler.handle(brokenResponse, errorCode))
                .doesNotThrowAnyException();
    }
}
