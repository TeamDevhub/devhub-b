package teamdevhub.devhub.outbound.terms.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.terms.domain.Terms;
import teamdevhub.devhub.core.terms.domain.TermsAgreement;
import teamdevhub.devhub.core.terms.port.out.TermsAgreementRepository;
import teamdevhub.devhub.core.terms.port.out.TermsRepository;
import teamdevhub.devhub.outbound.terms.adapter.entity.TermsAgreementEntity;
import teamdevhub.devhub.outbound.terms.adapter.mapper.TermsMapper;
import teamdevhub.devhub.outbound.terms.persistence.JpaTermsAgreementRepository;
import teamdevhub.devhub.outbound.terms.persistence.JpaTermsRepository;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class TermsAdapter implements TermsRepository, TermsAgreementRepository {

    private final JpaTermsRepository jpaTermsRepository;
    private final JpaTermsAgreementRepository jpaTermsAgreementRepository;

    @Override
    public PageResult<Terms> listTerms(PageCommand pageCommand) {
        return PageResult.of(null,0,0,0);
    }

    @Override
    public void saveTerms(Terms terms) {
        jpaTermsRepository.save(terms);
    }

    @Override
    public Terms findByTermsGuid(String termsGuid) {
        return jpaTermsRepository.findByTermsGuid(termsGuid)
                .map(TermsMapper::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("약관을 찾을 수 없습니다."));
    }

    @Override
    public List<Terms> findAllByTermsGuidIn(Set<String> termsGuids) {
        return jpaTermsRepository
                .findAllByTermsGuidInAndIsDeletedFalseAndIsUsedTrue(termsGuids)
                .stream()
                .map(TermsMapper::toDomain)
                .toList();
    }

    @Override
    public void saveAll(List<TermsAgreement> termsAgreementList) {
        List<TermsAgreementEntity> termsAgreementEntityList = termsAgreementList.stream()
                .map(TermsMapper::toEntity)
                .toList();

        jpaTermsAgreementRepository.saveAll(termsAgreementEntityList);
    }
}
