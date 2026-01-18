package teamdevhub.devhub.small.application.verification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.application.exception.BusinessRuleException;
import teamdevhub.devhub.application.verification.CompositeVerificationIssuerSelector;
import teamdevhub.devhub.application.verification.issuer.VerificationIssuer;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.domain.verification.vo.IssuedVerification;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.domain.verification.vo.VerificationType;
import teamdevhub.devhub.fake.pure.issuer.FakeEmailVerificationIssuer;
import teamdevhub.devhub.fake.pure.provider.FakeTimeProvider;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CompositeVerificationIssuerSelectorTest {

    private CompositeVerificationIssuerSelector compositeVerificationIssuerSelector;

    private static final LocalDateTime FIXED_TIME = LocalDateTime.of(2026,1,1,0,0);
    private static final String FIXED_CODE = "123456";

    @BeforeEach
    void init() {
        FakeTimeProvider fakeTimeProvider = new FakeTimeProvider(FIXED_TIME);

        VerificationIssuer emailVerificationIssuer = new FakeEmailVerificationIssuer(VerificationType.EMAIL, FIXED_CODE, fakeTimeProvider);

        compositeVerificationIssuerSelector = new CompositeVerificationIssuerSelector(List.of(emailVerificationIssuer));
    }

    @Test
    @DisplayName("EMAIL_타입이면_EMAIL_VerificationIssuer_가_선택되어_발급된다")
    void issueVerification_emailType_selectsEmailIssuer() {
        // given
        VerificationTarget verificationTarget = new VerificationTarget(VerificationType.EMAIL,"user@example.com");

        // when
        IssuedVerification issuedVerification = compositeVerificationIssuerSelector.issueVerification(verificationTarget);

        // then
        assertThat(issuedVerification.verification().getCode()).isEqualTo(FIXED_CODE);
        assertThat(issuedVerification.verification().getExpiredAt())
                .isEqualTo(FIXED_TIME.plusMinutes(5));
    }

    @Test
    @DisplayName("지원하지_않는_타입이면_BusinessRuleException_이_발생한다")
    void issueVerification_unsupportedType_throwsException() {
        // given
        VerificationTarget verificationTarget = new VerificationTarget(VerificationType.SMS,"01012341234");

        // when, then
        assertThatThrownBy(() ->
                compositeVerificationIssuerSelector.issueVerification(verificationTarget))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.VERIFICATION_FAIL.getMessage());
    }
}
