package teamdevhub.devhub.core.terms.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Terms {

    private final String termsGuid;
    private final boolean required;

    @Builder
    private Terms(String termsGuid, boolean required) {
        this.termsGuid = termsGuid;
        this.required = required;
    }

    public void validateAgreement(boolean agreed) {
        if (required && !agreed) {
            throw new IllegalStateException("필수 약관은 동의해야 합니다.");
        }
    }

    public static Terms of(String termsGuid, boolean required) {
        return Terms.builder()
                .termsGuid(termsGuid)
                .required(required)
                .build();
    }
}