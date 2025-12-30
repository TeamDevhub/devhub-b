package teamdevhub.devhub.common.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.adapter.in.common.vo.ApiDataResponseVo;
import teamdevhub.devhub.common.enums.ErrorCodeEnum;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class CustomFilterExceptionHandler {

    private final ObjectMapper mapper = new ObjectMapper();

    public void handle(HttpServletResponse res, ErrorCodeEnum errorCodeEnum) {

        try {
            if (res.isCommitted()) {
                res.resetBuffer();
            }

            res.setStatus(errorCodeEnum.getStatus().value());
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");

            ApiDataResponseVo<?> result = ApiDataResponseVo.failureWithoutData(errorCodeEnum);
            String json = mapper.writeValueAsString(result);

            res.getWriter().write(json);
            res.getWriter().flush();

        } catch (Exception e) {
            log.error("FilterExceptionHandler response write error", e);
        }
    }

    public void handle(HttpServletResponse res, Throwable ex) {
        log.error("Filter exception occurred", ex);

        res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        String message = ex.getMessage();
        if (message == null || message.isBlank()) {
            message = "An unexpected error occurred";
        }

        Map<String, Object> errorBody = Map.of(
                "message", message
        );

        ApiDataResponseVo<?> result = ApiDataResponseVo.failureFromFilter(ex);

        try {
            res.getWriter().write(mapper.writeValueAsString(result));
        } catch (IOException e) {
            log.error("FilterExceptionHandler response write error", e);
        }
    }
}
