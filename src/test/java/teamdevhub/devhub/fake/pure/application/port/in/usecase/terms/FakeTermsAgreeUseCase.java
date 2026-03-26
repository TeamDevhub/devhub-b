package teamdevhub.devhub.fake.pure.application.port.in.usecase.terms;

import teamdevhub.devhub.core.terms.domain.Terms;
import teamdevhub.devhub.core.terms.port.in.command.AgreeTermsCommand;
import teamdevhub.devhub.core.terms.port.in.command.CreateTermsCommand;
import teamdevhub.devhub.core.terms.port.in.usecase.TermsUseCase;

import java.util.List;

public class FakeTermsAgreeUseCase implements TermsUseCase {

    private boolean called = false;
    private AgreeTermsCommand agreeTermsCommand;

    @Override
    public List<Terms> listTerms() {
        return null;
    }

    @Override
    public void registerTerms(CreateTermsCommand createTermsCommand) {

    }

    @Override
    public void saveTermsAgreement(AgreeTermsCommand agreeTermsCommand) {
        this.called = true;
        this.agreeTermsCommand = agreeTermsCommand;
    }

    public AgreeTermsCommand getAgreeTermsCommand() {
        return agreeTermsCommand;
    }
}