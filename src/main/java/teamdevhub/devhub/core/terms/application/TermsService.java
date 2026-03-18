package teamdevhub.devhub.core.terms.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.core.terms.domain.Terms;
import teamdevhub.devhub.core.terms.port.in.command.CreateTermsCommand;
import teamdevhub.devhub.core.terms.port.in.usecase.TermsUseCase;
import teamdevhub.devhub.core.terms.port.out.TermsRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class TermsService implements TermsUseCase {

    private final TermsRepository termsRepository;
    private final IdentifierProvider identifierProvider;

    @Override
    public PageResult<Terms> listTerms(PageCommand pageCommand) {
        return termsRepository.listTerms(pageCommand);
    }

    @Override
    public void registerTerms(CreateTermsCommand createTermsCommand) {
        Terms terms = Terms.createTerms(createTermsCommand, identifierProvider.generateIdentifier());
        termsRepository.saveTerms(terms);
    }
}
