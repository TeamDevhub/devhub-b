package teamdevhub.devhub.outbound.terms.adapter.mapper;

import teamdevhub.devhub.core.terms.domain.Terms;
import teamdevhub.devhub.core.terms.domain.UserTermsAgreement;
import teamdevhub.devhub.outbound.terms.adapter.entity.TermsAgreementEntity;
import teamdevhub.devhub.outbound.terms.adapter.entity.TermsEntity;

public class TermsMapper {

    public static Terms toDomain(TermsEntity entity) {
        return Terms.of(
                entity.getTermsGuid(),
                entity.isRequired()
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
