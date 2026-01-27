package teamdevhub.devhub.shared.exception;

import teamdevhub.devhub.shared.web.model.response.DataApiResponse;
import teamdevhub.devhub.shared.enums.ErrorCode;
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
    public ResponseEntity<DataApiResponse<?>> handleDomainException(DomainRuleException e) {
        logException(e);
        return ResponseEntity.badRequest()
                .body(DataApiResponse.failureWithoutData(e.getErrorCode()));
    }

    @ExceptionHandler(BusinessRuleException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<DataApiResponse<?>> handleBusinessRuleException(BusinessRuleException e) {
        logException(e);
        return ResponseEntity.badRequest()
                .body(DataApiResponse.failureWithoutData(e.getErrorCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<DataApiResponse<?>> handleValidationException(MethodArgumentNotValidException methodArgumentNotValidException) {
        logException(methodArgumentNotValidException);
        String message = methodArgumentNotValidException.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse(ErrorCode.VALIDATION_FAIL.getMessage());

        return ResponseEntity.badRequest()
                .body(DataApiResponse.failureWithMessage(ErrorCode.VALIDATION_FAIL, message)
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<DataApiResponse<?>> handleException(Exception e) {
        logException(e);
        return ResponseEntity.badRequest()
                .body(DataApiResponse.failureFromThrowable(e));
    }
}