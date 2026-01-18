package teamdevhub.devhub.domain.verification.vo;

import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.domain.exception.DomainRuleException;

public enum VerificationType {

    EMAIL {
        @Override
        void validate(String value) {
            if (!value.matches("^[^@]+@[^@]+\\.[^@]+$")) {
                throw DomainRuleException.of(ErrorCode.INVALID_EMAIL_FORMAT);
            }
        }
    },
    SMS {
        @Override
        void validate(String value) {
            if (!value.matches("^01[0-9]{8,9}$")) {
                throw DomainRuleException.of(ErrorCode.INVALID_PHONE_NUMBER_FORMAT);
            }
        }
    },
    OTP {
        @Override
        void validate(String value) {
            if (value.isBlank()) {
                throw DomainRuleException.of(ErrorCode.OTP_BLANK);
            }
        }
    };

    abstract void validate(String value);
}