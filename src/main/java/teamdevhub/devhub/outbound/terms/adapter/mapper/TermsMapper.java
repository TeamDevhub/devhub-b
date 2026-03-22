package teamdevhub.devhub.outbound.terms.adapter.mapper;

import teamdevhub.devhub.core.terms.domain.Terms;
import teamdevhub.devhub.core.terms.domain.TermsAgreement;
import teamdevhub.devhub.outbound.terms.adapter.entity.TermsAgreementEntity;
import teamdevhub.devhub.outbound.terms.adapter.entity.TermsEntity;

public class TermsMapper {

    public static Terms toDomain(TermsEntity termsEntity) {
        return Terms.of(
                termsEntity.getTermsGuid(),
                termsEntity.getTitle(),
                termsEntity.getContent(),
                termsEntity.isRequired(),
                termsEntity.isUsed(),
                termsEntity.isDeleted()
        );
    }

    public static TermsAgreementEntity toEntity(TermsAgreement termsAgreement) {
        return TermsAgreementEntity.builder()
                .termsAgreementGuid(termsAgreement.getTermsAgreementGuid())
                .termsGuid(termsAgreement.getTermsGuid())
                .userGuid(termsAgreement.getUserGuid())
                .isAgreed(termsAgreement.isAgreed())
                .build();
    }
}
