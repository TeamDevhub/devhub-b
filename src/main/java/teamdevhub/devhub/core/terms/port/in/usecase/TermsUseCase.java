package teamdevhub.devhub.core.terms.port.in.usecase;

import teamdevhub.devhub.core.terms.domain.Terms;
import teamdevhub.devhub.core.terms.port.in.command.AgreeTermsCommand;
import teamdevhub.devhub.core.terms.port.in.command.CreateTermsCommand;

import java.util.List;

public interface TermsUseCase {

    List<Terms> listTerms();
    void registerTerms(CreateTermsCommand createTermsCommand);
    void saveTermsAgreement(AgreeTermsCommand agreeTermsCommand);
}
