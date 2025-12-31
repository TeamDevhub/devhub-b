package teamdevhub.devhub.common.validation.regex;

import teamdevhub.devhub.common.enums.RegexPatternEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RegexValidator implements ConstraintValidator<RegexMatch, String> {

    private RegexPatternEnum patternEnum;

    @Override
    public void initialize(RegexMatch constraintAnnotation) {
        this.patternEnum = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null) return true;

        boolean matched = value.matches(patternEnum.regexp());

        if (!matched) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(patternEnum.message())
                    .addConstraintViolation();
        }

        return matched;
    }
}