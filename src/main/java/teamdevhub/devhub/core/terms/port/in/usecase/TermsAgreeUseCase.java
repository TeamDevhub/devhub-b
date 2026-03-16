package teamdevhub.devhub.core.terms.port.in.usecase;

import teamdevhub.devhub.core.terms.port.in.command.AgreeTermsCommand;

public interface TermsAgreeUseCase {

    void agreeTerms(AgreeTermsCommand agreeTermsCommand);
}
