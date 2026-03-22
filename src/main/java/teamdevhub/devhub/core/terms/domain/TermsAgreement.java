package teamdevhub.devhub.core.terms.domain;

import lombok.Getter;

@Getter
public class TermsAgreement {

    private String termsAgreementGuid;
    private String termsGuid;
    private String userGuid;
    private boolean isAgreed;

    private TermsAgreement(
            String termsAgreementGuid,
            String termsGuid,
            String userGuid,
            boolean isAgreed
    ) {
        this.termsAgreementGuid = termsAgreementGuid;
        this.termsGuid = termsGuid;
        this.userGuid = userGuid;
        this.isAgreed = isAgreed;
    }

    public static TermsAgreement of(
            String termsAgreementGuid,
            String termsGuid,
            String userGuid,
            boolean isAgreed
    ) {
        return new TermsAgreement(
                termsAgreementGuid,
                termsGuid,
                userGuid,
                isAgreed
        );
    }
}
