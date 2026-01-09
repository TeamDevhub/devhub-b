package teamdevhub.devhub.medium.common.exception;

import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.common.exception.GlobalExceptionHandler;
import teamdevhub.devhub.domain.exception.DomainRuleException;
import teamdevhub.devhub.service.exception.BusinessRuleException;

import java.lang.reflect.Method;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
            .setControllerAdvice(exceptionHandler)
            .build();

    @RestController
    class TestController {
        @GetMapping("/domain-exception")
        public void domainException() {
            throw DomainRuleException.of(ErrorCode.EMAIL_DUPLICATED);
        }

        @GetMapping("/business-exception")
        public void businessException() {
            throw BusinessRuleException.of(ErrorCode.EMAIL_NOT_CONFIRMED);
        }

        @GetMapping("/validation-exception")
        public void validationException() throws Exception {
            throw new MethodArgumentNotValidException(
                    getFakeMethodParameter(),
                    new BindException(new Object(), ErrorCode.VALIDATION_FAIL.getCode())
            );
            }
        @GetMapping("/generic-exception")
        public void genericException() {
            throw new RuntimeException("테스트 에러");
        }
    }

    private MethodParameter getFakeMethodParameter() throws NoSuchMethodException {
        Method method = this.getClass().getDeclaredMethod("dummyMethod", String.class);
        return new MethodParameter(method, 0);
    }

    @SuppressWarnings("unused")
    private void dummyMethod(String param) {}

    @Test
    void 도메인예외_발생시_BAD_REQUEST_반환() throws Exception {
        // given, when
        mockMvc.perform(get("/domain-exception"))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value(ErrorCode.EMAIL_DUPLICATED.getCode()));
    }

    @Test
    void 비즈니스예외_발생시_BAD_REQUEST_반환() throws Exception {
        // given, when
        mockMvc.perform(get("/business-exception"))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value(ErrorCode.EMAIL_NOT_CONFIRMED.getCode()));
    }

    @Test
    void 검증예외_발생시_BAD_REQUEST_반환() throws Exception {
        // given, when
        mockMvc.perform(get("/validation-exception"))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value(ErrorCode.VALIDATION_FAIL.getCode())); // VALIDATION_FAIL 코드
    }

    @Test
    void 기타예외_발생시_BAD_REQUEST_반환() throws Exception {
        // given, when
        mockMvc.perform(get("/generic-exception"))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.message").value("테스트 에러"));
    }
}
