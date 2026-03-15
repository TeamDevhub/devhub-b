package teamdevhub.devhub.outbound.terms.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.terms.domain.Terms;
import teamdevhub.devhub.core.terms.domain.TermsAgreement;
import teamdevhub.devhub.core.terms.port.out.TermsAgreementRepository;
import teamdevhub.devhub.core.terms.port.out.TermsRepository;
import teamdevhub.devhub.outbound.terms.adapter.mapper.TermsMapper;
import teamdevhub.devhub.outbound.terms.persistence.JpaTermsAgreementRepository;
import teamdevhub.devhub.outbound.terms.persistence.JpaTermsRepository;

import static teamdevhub.devhub.outbound.terms.adapter.mapper.TermsMapper.toEntity;

@Component
@RequiredArgsConstructor
public class TermsAdapter implements TermsRepository, TermsAgreementRepository {

    private final JpaTermsRepository jpaTermsRepository;
    private final JpaTermsAgreementRepository jpaTermsAgreementRepository;

    @Override
    public Terms findByTermsGuid(String termsGuid) {
        return jpaTermsRepository.findByTermsGuid(termsGuid)
                .map(TermsMapper::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("약관을 찾을 수 없습니다."));
    }

    @Override
    public void save(TermsAgreement agreement) {
        jpaTermsAgreementRepository.save(toEntity(agreement));
    }
}
