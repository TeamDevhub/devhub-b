package teamdevhub.devhub.core.terms.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.terms.domain.Terms;
import teamdevhub.devhub.core.terms.port.in.command.CreateTermsCommand;
import teamdevhub.devhub.core.terms.port.in.usecase.TermsUseCase;

@Service
@Transactional
@RequiredArgsConstructor
public class TermsService implements TermsUseCase {

    @Override
    public PageResult<Terms> listTerms(PageCommand pageCommand) {
        return null;
    }

    @Override
    public void registerTerms(CreateTermsCommand createTermsCommand) {

    }
}
