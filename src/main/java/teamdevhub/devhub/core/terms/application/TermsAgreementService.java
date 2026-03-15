package teamdevhub.devhub.core.terms.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.core.terms.domain.Terms;
import teamdevhub.devhub.core.terms.domain.TermsAgreement;
import teamdevhub.devhub.core.terms.port.in.command.TermsAgreementCommand;
import teamdevhub.devhub.core.terms.port.in.usecase.TermsAgreementUseCase;
import teamdevhub.devhub.core.terms.port.out.TermsAgreementRepository;
import teamdevhub.devhub.core.terms.port.out.TermsRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class TermsAgreementService implements TermsAgreementUseCase {

    private final TermsRepository termsRepository;
    private final TermsAgreementRepository termsAgreementRepository;
    private final IdentifierProvider identifierProvider;

    @Override
    public void agreeTerms(TermsAgreementCommand command) {

        for (var agreement : command.termsAgreementList()) {

            Terms terms = termsRepository.findByTermsGuid(agreement.termsGuid());

            TermsAgreement userTermsAgreement = TermsAgreement.create(
                    terms,
                    identifierProvider.generateIdentifier(),
                    command.userGuid(),
                    agreement.isAgreed()
            );

            termsAgreementRepository.save(userTermsAgreement);
        }
    }
}
