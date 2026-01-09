package teamdevhub.devhub.adapter.in.web.validator;

import teamdevhub.devhub.common.enums.RegexPattern;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RegexValidator.class)
public @interface RegexMatch {

    RegexPattern value();

    String message() default "잘못된 형식입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}