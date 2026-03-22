package teamdevhub.devhub.outbound.terms.adapter.mapper;

import teamdevhub.devhub.core.terms.domain.TermsAgreement;
import teamdevhub.devhub.outbound.terms.adapter.entity.TermsAgreementEntity;

public class TermsAgreementMapper {

    public TermsAgreementEntity termsAgreementEntity(TermsAgreement termsAgreement) {
        return TermsAgreementEntity.builder()
                .termsAgreementGuid(termsAgreement.getTermsAgreementGuid())
                .termsGuid(termsAgreement.getTermsGuid())
                .userGuid(termsAgreement.getUserGuid())
                .isAgreed(termsAgreement.isAgreed())
                .build();
    }

    public TermsAgreement toDomain(TermsAgreementEntity entity) {
        return TermsAgreement.of(
                entity.getTermsAgreementGuid(),
                entity.getTermsGuid(),
                entity.getUserGuid(),
                entity.isAgreed()
        );
    }
}
