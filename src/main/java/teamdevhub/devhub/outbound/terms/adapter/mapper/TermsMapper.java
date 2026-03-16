package teamdevhub.devhub.outbound.terms.adapter.mapper;

import teamdevhub.devhub.core.terms.domain.Terms;
import teamdevhub.devhub.core.terms.domain.UserTermsAgreement;
import teamdevhub.devhub.outbound.terms.adapter.entity.TermsAgreementEntity;
import teamdevhub.devhub.outbound.terms.adapter.entity.TermsEntity;

public class TermsMapper {

    public static Terms toDomain(TermsEntity entity) {
        return Terms.of(
                entity.getTermsGuid(),
                entity.getTitle(),
                entity.getContent(),
                entity.isRequired(),
                entity.isUsed(),
                entity.isDeleted()
        );
    }

    public static TermsAgreementEntity toEntity(UserTermsAgreement domain) {
        return TermsAgreementEntity.builder()
                .termsAgreementGuid(domain.getTermsAgreementGuid())
                .termsGuid(domain.getTermsGuid())
                .userGuid(domain.getUserGuid())
                .isAgreed(domain.isAgreed())
                .build();
    }
}
