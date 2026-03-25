package teamdevhub.devhub.core.terms.port.out;

import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.terms.domain.Terms;

import java.util.List;
import java.util.Set;

public interface TermsRepository {

    PageResult<Terms> listTerms(PageCommand pageCommand);
    void saveTerms(Terms terms);
    List<Terms> findAllByTermsGuidIn(Set<String> termsGuids);
    Terms findByTermsGuid(String termsGuid);
}