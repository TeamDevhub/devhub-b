package teamdevhub.devhub.core.terms.port.out;

import teamdevhub.devhub.core.terms.domain.Terms;

public interface TermsRepository {

    Terms findByTermsGuid(String termsGuid);
}