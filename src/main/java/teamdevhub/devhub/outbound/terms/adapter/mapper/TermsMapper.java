package teamdevhub.devhub.outbound.terms.adapter.mapper;

import teamdevhub.devhub.core.terms.domain.Terms;
import teamdevhub.devhub.core.terms.domain.UserTermsAgreement;
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

    public static TermsAgreementEntity toEntity(UserTermsAgreement userTermsAgreement) {
        return TermsAgreementEntity.builder()
                .termsAgreementGuid(userTermsAgreement.getTermsAgreementGuid())
                .termsGuid(userTermsAgreement.getTermsGuid())
                .userGuid(userTermsAgreement.getUserGuid())
                .isAgreed(userTermsAgreement.isAgreed())
                .build();
    }
}
