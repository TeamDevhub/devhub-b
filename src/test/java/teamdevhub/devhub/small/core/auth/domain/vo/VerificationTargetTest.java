package teamdevhub.devhub.small.core.auth.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.core.common.exception.DomainRuleException;
import teamdevhub.devhub.core.auth.domain.vo.VerificationTarget;
import teamdevhub.devhub.core.auth.domain.vo.VerificationType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class VerificationTargetTest {

    @Test
    @DisplayName("EMAIL_타입과_올바른_이메일_값으로_VerificationTarget_생성에_성공한다")
    void createVerificationTargetWithValidEmail() {
        VerificationTarget verificationTarget= VerificationTarget.of(VerificationType.EMAIL, TEST_EMAIL);

        assertThat(verificationTarget.verificationType()).isEqualTo(VerificationType.EMAIL);
        assertThat(verificationTarget.value()).isEqualTo(TEST_EMAIL);
    }

    @Test
    @DisplayName("EMAIL_타입과_잘못된_이메일_형식이면_예외를_던진다")
    void throwExceptionWhenInvalidEmailFormat() {
        assertThatThrownBy(() ->
                VerificationTarget.of(VerificationType.EMAIL, "invalid-email"))
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining(ErrorCode.INVALID_EMAIL_FORMAT.getMessage());
    }

    @Test
    @DisplayName("SMS_타입과_올바른_전화번호_값으로_VerificationTarget_생성에_성공한다")
    void createVerificationTargetWithValidPhone() {
        VerificationTarget verificationTarget= VerificationTarget.of(VerificationType.SMS, "01012345678");

        assertThat(verificationTarget.verificationType()).isEqualTo(VerificationType.SMS);
        assertThat(verificationTarget.value()).isEqualTo("01012345678");
    }

    @Test
    @DisplayName("SMS_타입과_잘못된_전화번호_형식이면_예외를_던진다")
    void throwExceptionWhenInvalidPhoneFormat() {
        assertThatThrownBy(() ->
                VerificationTarget.of(VerificationType.SMS, "123456"))
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining(ErrorCode.INVALID_PHONE_NUMBER_FORMAT.getMessage());
    }

    @Test
    @DisplayName("OTP_타입과_공백이_아닌_값으로_VerificationTarget_생성에_성공한다")
    void createVerificationTargetWithValidOtp() {
        VerificationTarget verificationTarget= VerificationTarget.of(VerificationType.OTP, "123456");

        assertThat(verificationTarget.verificationType()).isEqualTo(VerificationType.OTP);
        assertThat(verificationTarget.value()).isEqualTo("123456");
    }

    @Test
    @DisplayName("OTP_타입과_공백_값이면_예외를_던진다")
    void throwExceptionWhenOtpIsBlank() {
        assertThatThrownBy(() ->
                VerificationTarget.of(VerificationType.OTP, " "))
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining(ErrorCode.OTP_BLANK.getMessage());
    }

    @Test
    @DisplayName("verificationType_이_null_이면_예외를_던진다")
    void throwExceptionWhenVerificationTypeIsNull() {
        assertThatThrownBy(() ->
                VerificationTarget.of(null, "test@test.com"))
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining(ErrorCode.VERIFICATION_TYPE_INVALID.getMessage());
    }

    @Test
    @DisplayName("value_가_null_이면_예외를_던진다")
    void throwExceptionWhenValueIsNull() {
        assertThatThrownBy(() ->
                VerificationTarget.of(VerificationType.EMAIL, null))
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining(ErrorCode.VERIFICATION_VALUE_REQUIRED.getMessage());
    }
}
