package teamdevhub.devhub.core.terms.port.out;

import teamdevhub.devhub.core.terms.domain.TermsAgreement;

import java.util.List;

public interface TermsAgreementRepository {

    void saveAll(List<TermsAgreement> termsAgreementList);
}
