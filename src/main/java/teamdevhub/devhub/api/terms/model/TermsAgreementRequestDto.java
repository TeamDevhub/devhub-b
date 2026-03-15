package teamdevhub.devhub.api.terms.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TermsAgreementRequestDto {

    @NotBlank
    private String termsGuid;

    @NotNull(message = "약관 동의 여부는 필수입니다")
    private boolean isAgreed;

}
