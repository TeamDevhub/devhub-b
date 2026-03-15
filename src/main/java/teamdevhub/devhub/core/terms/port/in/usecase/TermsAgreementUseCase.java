package teamdevhub.devhub.core.terms.port.in.usecase;

import teamdevhub.devhub.core.terms.port.in.command.TermsAgreementCommand;

public interface TermsAgreementUseCase {

    void agreeTerms(TermsAgreementCommand termsAgreementCommand);
}
