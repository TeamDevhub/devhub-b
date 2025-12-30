package teamdevhub.devhub.common.exception;

import teamdevhub.devhub.adapter.in.common.vo.ApiResponseVo;
import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger errorFileLogger = LoggerFactory.getLogger("ERROR_FILE");

    private void logException(Exception e) {
        String traceId = MDC.get("traceId");
        errorFileLogger.error("[{}] -----GlobalExceptionHandler-----: {}", traceId, e.getMessage(), e);
        log.error("[{}] -----GlobalExceptionHandler-----: {}", traceId, e.getMessage(), e);
    }

    @ExceptionHandler(DomainRuleException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponseVo<?>> handleDomainException(DomainRuleException e) {
        logException(e);
        return ResponseEntity.badRequest()
                .body(ApiResponseVo.failureWithoutData(e.getErrorCodeEnum()));
    }

    @ExceptionHandler(BusinessRuleException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponseVo<?>> handleBusinessRuleException(BusinessRuleException e) {
        logException(e);
        return ResponseEntity.badRequest()
                .body(ApiResponseVo.failureWithoutData(e.getErrorCodeEnum()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponseVo<?>> handleValidationException(MethodArgumentNotValidException e) {
        logException(e);
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("검증 오류");

        return ResponseEntity.badRequest()
                .body(ApiResponseVo.failureWithMessage(ErrorCodeEnum.VALIDATION_FAIL, message)
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ApiResponseVo<?>> handleException(Exception e) {
        logException(e);
        return ResponseEntity.badRequest()
                .body(ApiResponseVo.failureFromThrowable(e));
    }
}