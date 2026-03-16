package teamdevhub.devhub.core.terms.port.in.usecase;

import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.terms.domain.Terms;
import teamdevhub.devhub.core.terms.port.in.command.CreateTermsCommand;

public interface TermsUseCase {


    PageResult<Terms> listTerms(PageCommand pageCommand);
    void registerTerms(CreateTermsCommand createTermsCommand);
}
