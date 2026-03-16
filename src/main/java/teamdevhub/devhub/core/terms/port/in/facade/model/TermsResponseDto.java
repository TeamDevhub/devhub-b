package teamdevhub.devhub.core.terms.port.in.facade.model;

import teamdevhub.devhub.core.terms.domain.Terms;

public class TermsResponseDto {
    public static TermsResponseDto fromDomain(Terms terms) {
        return new TermsResponseDto();
    }
}
