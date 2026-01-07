package teamdevhub.devhub.adapter.in.common.validator;

import teamdevhub.devhub.adapter.in.common.annotation.RegexMatch;
import teamdevhub.devhub.common.enums.RegexPattern;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RegexValidator implements ConstraintValidator<RegexMatch, String> {

    private RegexPattern regexPattern;

    @Override
    public void initialize(RegexMatch constraintAnnotation) {
        this.regexPattern = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null) return true;

        boolean matched = value.matches(regexPattern.regexp());

        if (!matched) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(regexPattern.message())
                    .addConstraintViolation();
        }

        return matched;
    }
}