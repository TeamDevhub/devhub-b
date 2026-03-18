package teamdevhub.devhub.fake.pure.application.port.in.usecase.terms;

import teamdevhub.devhub.core.terms.port.in.command.AgreeTermsCommand;
import teamdevhub.devhub.core.terms.port.in.usecase.TermsAgreeUseCase;

public class FakeTermsAgreeUseCase implements TermsAgreeUseCase {

    private boolean called = false;
    private AgreeTermsCommand agreeTermsCommand;

    @Override
    public void agreeTerms(AgreeTermsCommand agreeTermsCommand) {
        this.called = true;
        this.agreeTermsCommand = agreeTermsCommand;
    }

    public AgreeTermsCommand getAgreeTermsCommand() {
        return agreeTermsCommand;
    }
}