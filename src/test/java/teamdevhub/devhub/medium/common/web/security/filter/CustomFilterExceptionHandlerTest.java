package teamdevhub.devhub.medium.common.web.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import teamdevhub.devhub.adapter.in.web.dto.response.ApiDataResponseDto;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.common.web.security.filter.CustomFilterExceptionHandler;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomFilterExceptionHandlerTest {

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
    void httpServletResponse_에_에러코드_응답을_JSON_으로_작성한다() throws Exception {
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
}
