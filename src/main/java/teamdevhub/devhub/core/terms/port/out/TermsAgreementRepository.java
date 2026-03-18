package teamdevhub.devhub.core.terms.port.out;

import teamdevhub.devhub.core.terms.domain.UserTermsAgreement;

public interface TermsAgreementRepository {

    void saveUserTermsAgreement(UserTermsAgreement agreement);
}
