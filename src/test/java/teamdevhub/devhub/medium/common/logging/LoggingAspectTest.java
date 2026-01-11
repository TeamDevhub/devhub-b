package teamdevhub.devhub.medium.common.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import teamdevhub.devhub.common.logging.LoggingAspect;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LoggingAspectTest {

    private final LoggingAspect loggingAspect = new LoggingAspect(new ObjectMapper());

    @Test
    @DisplayName("logAround_는_시작과_끝을_로그한다")
    void logStartAndEnd() throws Throwable {
        // given
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        DummyService target = new DummyService();
        when(joinPoint.getTarget()).thenReturn(target);
        when(joinPoint.getArgs()).thenReturn(new Object[]{"world"});

        // when
        MethodSignature methodSignature = mock(MethodSignature.class);
        when(methodSignature.getName()).thenReturn("hello");
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(joinPoint.proceed()).thenReturn("HELLO WORLD");
        Object result = loggingAspect.logAround(joinPoint);

        // then
        assertThat(result).isEqualTo("HELLO WORLD");
    }

    static class DummyService {
        public String hello(String name) {
            return "HELLO " + name;
        }
    }
}
