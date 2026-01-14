package teamdevhub.devhub.medium.common.exception;

import org.junit.jupiter.api.DisplayName;
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

class GlobalExceptionHandlerMediumTest {

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
            throw new RuntimeException("Test Error Occurred");
        }
    }

    private MethodParameter getFakeMethodParameter() throws NoSuchMethodException {
        Method method = this.getClass().getDeclaredMethod("dummyMethod", String.class);
        return new MethodParameter(method, 0);
    }

    @SuppressWarnings("unused")
    private void dummyMethod(String param) {}

    @Test
    @DisplayName("DomainRuleException_이_발생하면_BAD_REQUEST_반환")
    void returnBadRequestOnDomainRuleException() throws Exception {
        // given, when
        mockMvc.perform(get("/domain-exception"))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value(ErrorCode.EMAIL_DUPLICATED.getCode()));
    }

    @Test
    @DisplayName("BusinessRuleException_이_발생하면_BAD_REQUEST_반환")
    void returnBadRequestOnBusinessRuleException() throws Exception {
        // given, when
        mockMvc.perform(get("/business-exception"))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value(ErrorCode.EMAIL_NOT_CONFIRMED.getCode()));
    }

    @Test
    @DisplayName("MethodArgumentNotValidException_이_발생하면_BAD_REQUEST_반환")
    void returnBadRequestOnMethodArgumentNotValidException() throws Exception {
        // given, when
        mockMvc.perform(get("/validation-exception"))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value(ErrorCode.VALIDATION_FAIL.getCode()));
    }

    @Test
    @DisplayName("기타예외가_발생하면_BAD_REQUEST_반환")
    void returnBadRequestOnOtherExceptions() throws Exception {
        // given, when
        mockMvc.perform(get("/generic-exception"))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.message").value("Test Error Occurred"));
    }
}
