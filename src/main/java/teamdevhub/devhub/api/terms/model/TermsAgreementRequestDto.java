package teamdevhub.devhub.api.terms.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.core.terms.domain.TermsAgreementItem;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TermsAgreementRequestDto {

    @NotBlank
    private String termsGuid;

    @NotNull
    private boolean isAgreed;

    public TermsAgreementItem toTermsAgreementItem() {
        return new TermsAgreementItem(
                this.termsGuid,
                this.isAgreed
        );
    }
}
