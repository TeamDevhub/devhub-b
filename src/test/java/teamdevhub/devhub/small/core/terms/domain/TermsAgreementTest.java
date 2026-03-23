package teamdevhub.devhub.small.core.terms.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.terms.domain.TermsAgreement;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.TermsTestConstant.*;

public class TermsAgreementTest {

    @Test
    @DisplayName("약관_동의_객체를_생성한다")
    void createTermsAgreement() {
        // when
        TermsAgreement agreement = createAgreement(AGREEMENT_GUID_1, TERMS_GUID_1, USER_GUID_1, AGREED);

        // then
        assertThat(agreement.getTermsAgreementGuid()).isEqualTo(AGREEMENT_GUID_1);
        assertThat(agreement.getTermsGuid()).isEqualTo(TERMS_GUID_1);
        assertThat(agreement.getUserGuid()).isEqualTo(USER_GUID_1);
        assertThat(agreement.isAgreed()).isTrue();
    }

    @Test
    @DisplayName("약관_미동의_상태로도_생성할_수_있다")
    void createNotAgreedTermsAgreement() {
        // when
        TermsAgreement agreement = createAgreement(AGREEMENT_GUID_2, TERMS_GUID_2, USER_GUID_2, NOT_AGREED);

        // then
        assertThat(agreement.getTermsAgreementGuid()).isEqualTo(AGREEMENT_GUID_2);
        assertThat(agreement.getTermsGuid()).isEqualTo(TERMS_GUID_2);
        assertThat(agreement.getUserGuid()).isEqualTo(USER_GUID_2);
        assertThat(agreement.isAgreed()).isFalse();
    }

    private TermsAgreement createAgreement(
            String agreementGuid,
            String termsGuid,
            String userGuid,
            boolean isAgreed
    ) {
        return TermsAgreement.of(
                agreementGuid,
                termsGuid,
                userGuid,
                isAgreed
        );
    }
}
