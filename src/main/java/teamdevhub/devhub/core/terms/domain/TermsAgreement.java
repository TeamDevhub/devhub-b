package teamdevhub.devhub.core.terms.domain;

import lombok.Getter;

@Getter
public class TermsAgreement {

    private String termsAgreementGuid;
    private String termsGuid;
    private String userGuid;
    private boolean agreed;

    private TermsAgreement(
            String termsAgreementGuid,
            String termsGuid,
            String userGuid,
            boolean agreed
    ) {
        this.termsAgreementGuid = termsAgreementGuid;
        this.termsGuid = termsGuid;
        this.userGuid = userGuid;
        this.agreed = agreed;
    }

    public static TermsAgreement create(
            Terms terms,
            String agreementGuid,
            String userGuid,
            boolean agreed
    ) {

        terms.validateAgreement(agreed);

        return new TermsAgreement(
                agreementGuid,
                terms.getTermsGuid(),
                userGuid,
                agreed
        );
    }
}
