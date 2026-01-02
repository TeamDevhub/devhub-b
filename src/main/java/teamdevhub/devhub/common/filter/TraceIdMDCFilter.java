package teamdevhub.devhub.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class TraceIdMDCFilter extends OncePerRequestFilter {

    private static final String TRACE_ID_KEY = "traceId";

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            String traceId = MDC.get(TRACE_ID_KEY);
            if (traceId == null) {
                traceId = httpServletRequest.getHeader("X-Trace-Id");

                if (traceId == null || traceId.isBlank()) {
                    traceId = UUID.randomUUID()
                            .toString()
                            .replace("-", "")
                            .substring(0, 16);
                }

                MDC.put(TRACE_ID_KEY, traceId);
            }

            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } finally {
            MDC.remove(TRACE_ID_KEY);
        }
    }
}