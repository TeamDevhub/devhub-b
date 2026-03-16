package teamdevhub.devhub.core.terms.port.in.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.terms.domain.Terms;
import teamdevhub.devhub.core.terms.port.in.command.CreateTermsCommand;
import teamdevhub.devhub.core.terms.port.in.usecase.TermsUseCase;

@Service
@Transactional
@RequiredArgsConstructor
public class TermsFacade {

    private final TermsUseCase termsUseCase;

    public PageResult<Terms> listTerms(PageCommand pageCommand) {
        return termsUseCase.listTerms(pageCommand);
    }

    public void registerTerms(CreateTermsCommand createTermsCommand) {
        termsUseCase.registerTerms(createTermsCommand);
    }
}
