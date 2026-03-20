package teamdevhub.devhub.core.terms.port.out;

import teamdevhub.devhub.core.terms.domain.UserTermsAgreement;

import java.util.List;

public interface TermsAgreementRepository {

    void saveAll(List<UserTermsAgreement> userTermsAgreementList);
}
