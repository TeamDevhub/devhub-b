package teamdevhub.devhub.core.terms.domain;

import lombok.Getter;

@Getter
public class UserTermsAgreement {

    private String termsAgreementGuid;
    private String termsGuid;
    private String userGuid;
    private boolean isAgreed;

    private UserTermsAgreement(
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

    public static UserTermsAgreement create(
            Terms terms,
            String agreementGuid,
            String userGuid,
            boolean agreed
    ) {

        terms.validateAgreement(agreed);
        return new UserTermsAgreement(
                agreementGuid,
                terms.getTermsGuid(),
                userGuid,
                agreed
        );
    }
}
