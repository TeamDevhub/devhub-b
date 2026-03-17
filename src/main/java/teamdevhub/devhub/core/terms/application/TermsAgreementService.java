package teamdevhub.devhub.core.terms.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.core.terms.domain.Terms;
import teamdevhub.devhub.core.terms.domain.TermsAgreementItem;
import teamdevhub.devhub.core.terms.domain.UserTermsAgreement;
import teamdevhub.devhub.core.terms.port.in.command.AgreeTermsCommand;
import teamdevhub.devhub.core.terms.port.in.usecase.TermsAgreeUseCase;
import teamdevhub.devhub.core.terms.port.out.TermsAgreementRepository;
import teamdevhub.devhub.core.terms.port.out.TermsRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TermsAgreementService implements TermsAgreeUseCase {

    private final TermsRepository termsRepository;
    private final TermsAgreementRepository termsAgreementRepository;
    private final IdentifierProvider identifierProvider;

    @Override
    public void agreeTerms(AgreeTermsCommand command) {

        String userGuid = command.userGuid();
        List<TermsAgreementItem> agreements = command.termsAgreementItemList();

        for (TermsAgreementItem agreement : agreements) {

            String termsGuid = agreement.termsGuid();
            boolean agreed = agreement.agreed();

            Terms terms = termsRepository.findByTermsGuid(termsGuid);

            UserTermsAgreement userTermsAgreement = UserTermsAgreement.create(
                    terms,
                    identifierProvider.generateIdentifier(),
                    userGuid,
                    agreed
            );

            termsAgreementRepository.saveUserTermsAgreement(userTermsAgreement);
        }
    }
}
