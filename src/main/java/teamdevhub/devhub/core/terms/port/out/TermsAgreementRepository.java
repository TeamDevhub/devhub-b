package teamdevhub.devhub.core.terms.port.out;

import teamdevhub.devhub.core.terms.domain.TermsAgreement;

public interface TermsAgreementRepository {

    void save(TermsAgreement agreement);
}
