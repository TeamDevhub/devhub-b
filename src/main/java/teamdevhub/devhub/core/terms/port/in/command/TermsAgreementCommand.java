package teamdevhub.devhub.core.terms.port.in.command;

import java.util.List;

public record TermsAgreementCommand(String userGuid, List<TermsAgreement> termsAgreementList) {

    public record TermsAgreement(String termsGuid, boolean isAgreed) {
    }
}
