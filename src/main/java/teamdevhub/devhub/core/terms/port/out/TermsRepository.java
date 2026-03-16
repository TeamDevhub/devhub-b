package teamdevhub.devhub.core.terms.port.out;

import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.terms.domain.Terms;

public interface TermsRepository {

    PageResult<Terms> listTerms(PageCommand pageCommand);
    void save(Terms terms);
    Terms findByTermsGuid(String termsGuid);
}