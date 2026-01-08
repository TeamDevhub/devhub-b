package teamdevhub.devhub.common.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.adapter.in.common.vo.ApiDataResponseVo;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException accessDeniedException) throws IOException {
        ApiDataResponseVo<?> result = ApiDataResponseVo.failureFromThrowable(accessDeniedException);

        httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(mapper.writeValueAsString(result));
    }
}