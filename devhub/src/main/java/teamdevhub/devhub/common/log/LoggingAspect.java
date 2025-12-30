package teamdevhub.devhub.common.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

    private final ObjectMapper objectMapper;
    private static final String EMPTY_JSON = "[]";
    private static final String NULL_JSON = "null";
    private static final int SAMPLE_LIMIT = 5;

    @Around("execution(* teamdevhub.devhub..adapter.in.project..*(..)) || " +
            "execution(* teamdevhub.devhub..adapter.out.project..*(..)) || " +
            "execution(* teamdevhub.devhub..application.project..*(..)) ")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        String params = getParamsAsJson(joinPoint.getArgs());

        log.info("START {}.{}() with params: {}", className, methodName, params);

        Object result = joinPoint.proceed();
        long elapsed = System.currentTimeMillis() - start;
        String resultLog = summarizeResult(result);

        log.info("END {}.{}() in {} ms with result: {}", className, methodName, elapsed, resultLog);

        return result;
    }

    private String getParamsAsJson(Object[] args) {
        if (args == null || args.length == 0) {
            return EMPTY_JSON;
        }
        return toSafeJson(args);
    }

    private String summarizeResult(Object result) {
        if (result == null) {
            return NULL_JSON;
        }

        if (result instanceof Page<?> page) {
            return "Page{contentSize=%d, page=%d, size=%d, totalElements=%d}"
                    .formatted(
                            page.getNumberOfElements(),
                            page.getNumber(),
                            page.getSize(),
                            page.getTotalElements()
                    );
        }

        if (result instanceof Slice<?> slice) {
            return "Slice{contentSize=%d, hasNext=%s}"
                    .formatted(slice.getNumberOfElements(), slice.hasNext());
        }

        if (result instanceof Collection<?> coll) {
            return summarizeCollection(coll);
        }

        return toSafeJson(result);
    }

    private String summarizeCollection(Collection<?> coll) {
        int size = coll.size();

        if (size <= SAMPLE_LIMIT) {
            return toSafeJson(coll);
        }

        List<?> sample = coll.stream().limit(SAMPLE_LIMIT).toList();
        return "%s... (total %d items)".formatted(toSafeJson(sample), size);
    }

    private String toSafeJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return Optional.ofNullable(obj)
                    .map(Object::toString)
                    .orElse(NULL_JSON);
        }
    }
}
