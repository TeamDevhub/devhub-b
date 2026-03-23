package teamdevhub.devhub.core.terms.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.core.terms.domain.Terms;
import teamdevhub.devhub.core.terms.domain.TermsAgreementItem;
import teamdevhub.devhub.core.terms.domain.TermsAgreement;
import teamdevhub.devhub.core.terms.port.in.command.AgreeTermsCommand;
import teamdevhub.devhub.core.terms.port.in.usecase.TermsAgreeUseCase;
import teamdevhub.devhub.core.terms.port.out.TermsAgreementRepository;
import teamdevhub.devhub.core.terms.port.out.TermsRepository;
import teamdevhub.devhub.shared.enums.ErrorCode;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TermsAgreementService implements TermsAgreeUseCase {

    private final TermsRepository termsRepository;
    private final TermsAgreementRepository termsAgreementRepository;
    private final IdentifierProvider identifierProvider;

    @Override
    public void agreeTerms(AgreeTermsCommand agreeTermsCommand) {

        Map<String, Boolean> termsAgreementMap = toAgreementMap(agreeTermsCommand);

        List<Terms> termsList = termsRepository.findAllByTermsGuidIn(termsAgreementMap.keySet());
        validateAllTermsExist(termsList, termsAgreementMap);

        List<TermsAgreement> termsAgreementList = termsList.stream()
                .map(terms -> Terms.createAgreement(
                        terms,
                        identifierProvider.generateIdentifier(),
                        agreeTermsCommand.userGuid(),
                        termsAgreementMap.get(terms.getTermsGuid())
                ))
                .toList();

        termsAgreementRepository.saveAll(termsAgreementList);
    }

    private Map<String, Boolean> toAgreementMap(AgreeTermsCommand command) {
        return command.termsAgreementItemList()
                .stream()
                .collect(Collectors.toMap(
                        TermsAgreementItem::termsGuid,
                        TermsAgreementItem::isAgreed
                ));
    }

    private void validateAllTermsExist(List<Terms> termsList, Map<String, Boolean> agreementMap) {
        Set<String> foundTermsGuids = termsList.stream()
                .map(Terms::getTermsGuid)
                .collect(Collectors.toSet());

        if (!foundTermsGuids.equals(agreementMap.keySet())) {
            throw BusinessRuleException.of(ErrorCode.UNKNOWN_FAIL);
        }
    }
}
