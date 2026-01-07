package teamdevhub.devhub.common.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.adapter.in.common.vo.ApiDataResponseVo;
import teamdevhub.devhub.common.enums.ErrorCode;

@Slf4j
@Component
public class CustomFilterExceptionHandler {

    private final ObjectMapper mapper = new ObjectMapper();

    public void handle(HttpServletResponse httpServletResponse, ErrorCode errorCode) {

        try {
            if (httpServletResponse.isCommitted()) {
                httpServletResponse.resetBuffer();
            }

            httpServletResponse.setStatus(errorCode.getStatus().value());
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setCharacterEncoding("UTF-8");

            ApiDataResponseVo<?> result = ApiDataResponseVo.failureWithoutData(errorCode);
            String json = mapper.writeValueAsString(result);

            httpServletResponse.getWriter().write(json);
            httpServletResponse.getWriter().flush();

        } catch (Exception e) {
            log.error("FilterExceptionHandler response write error", e);
        }
    }
}
