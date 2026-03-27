package teamdevhub.devhub.core.terms.domain;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.core.common.exception.DomainRuleException;
import teamdevhub.devhub.core.terms.port.in.command.CreateTermsCommand;
import teamdevhub.devhub.shared.enums.ErrorCode;

@Getter
public class Terms {

    private final String termsGuid;
    private final String title;
    private final String content;
    private final boolean isRequired;
    private final boolean isUsed;
    private final boolean isDeleted;

    @Builder
    private Terms(
            String termsGuid,
            String title,
            String content,
            boolean isRequired,
            boolean isUsed,
            boolean isDeleted
    ) {
        this.termsGuid = termsGuid;
        this.title = title;
        this.content = content;
        this.isRequired = isRequired;
        this.isUsed = isUsed;
        this.isDeleted = isDeleted;
    }

    public static Terms of(
            String termsGuid,
            String title,
            String content,
            boolean isRequired,
            boolean isUsed,
            boolean isDeleted
    ) {
        return Terms.builder()
                .termsGuid(termsGuid)
                .title(title)
                .content(content)
                .isRequired(isRequired)
                .isUsed(isUsed)
                .isDeleted(isDeleted)
                .build();
    }

    public void validateAgreement(boolean isAgreed) {

        if (isDeleted) {
            throw DomainRuleException.of(ErrorCode.UNKNOWN_FAIL);
        }

        if (!isUsed) {
            throw DomainRuleException.of(ErrorCode.UNKNOWN_FAIL);
        }

        if (isRequired && !isAgreed) {
            throw DomainRuleException.of(ErrorCode.UNKNOWN_FAIL);
        }
    }

    public static Terms createTerms(CreateTermsCommand command, String termsGuid) {
        return Terms.builder()
                .termsGuid(termsGuid)
                .title(command.title())
                .content(command.content())
                .isRequired(command.isRequired())
                .isUsed(command.isUsed())
                .isDeleted(command.isDeleted())
                .build();
    }

    public static TermsAgreement createAgreement(
            Terms terms,
            String termsAgreementGuid,
            String userGuid,
            boolean isAgreed
    ) {
        terms.validateAgreement(isAgreed);

        return TermsAgreement.of(
                termsAgreementGuid,
                terms.getTermsGuid(),
                userGuid,
                isAgreed
        );
    }
}