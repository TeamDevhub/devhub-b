package teamdevhub.devhub.medium.common.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import teamdevhub.devhub.common.web.filter.TraceIdMDCFilter;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TraceIdMDCFilterTest {

    private final TraceIdMDCFilter filter = new TraceIdMDCFilter();

    @Test
    void 요청헤더에_traceId가_있으면_MDC_에_같은_traceId_를_설정한다() throws ServletException, IOException {
        // given
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.addHeader("X-Trace-Id", "existing-trace-id");
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        // when
        filter.doFilter(mockHttpServletRequest, mockHttpServletResponse, chain);

        // then
        assertThat(MDC.get("traceId")).isNull();
        verify(chain).doFilter(mockHttpServletRequest, mockHttpServletResponse);
    }

    @Test
    void 요청헤더가_없으면_MDC_에_새로운_traceId_를_생성해서_설정한다() throws ServletException, IOException {
        // given
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        // when
        filter.doFilter(mockHttpServletRequest, mockHttpServletResponse, chain);

        // then
        assertThat(MDC.get("traceId")).isNull();
        verify(chain).doFilter(mockHttpServletRequest, mockHttpServletResponse);
    }

    @Test
    void filter_실행후_MDC_는_항상_traceId_를_제거한다() throws ServletException, IOException {
        // given
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        FilterChain chain = (req, res) -> assertThat(MDC.get("traceId")).isNotNull();

        // when
        filter.doFilter(mockHttpServletRequest, mockHttpServletResponse, chain);

        // then
        assertThat(MDC.get("traceId")).isNull();
    }

    @Test
    void traceId_는_16자리_문자열로_생성된다() throws ServletException, IOException {
        // given
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        FilterChain chain = (req, res) -> {
            String traceId = MDC.get("traceId");
            assertThat(traceId).hasSize(16);
        };

        // when
        filter.doFilter(mockHttpServletRequest, mockHttpServletResponse, chain);

        // then
        assertThat(MDC.get("traceId")).isNull();
    }
}