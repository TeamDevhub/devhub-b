package teamdevhub.devhub.core.auth.port.in.command.oauth;

import lombok.Builder;
import teamdevhub.devhub.core.terms.domain.TermsAgreementItem;
import teamdevhub.devhub.core.terms.port.in.command.AgreeTermsCommand;
import teamdevhub.devhub.shared.enums.VerificationProvider;

import java.util.List;

@Builder
public record SignupOauthUserCommand(String tempToken, VerificationProvider verificationProvider, String username, String introduction,
                                     List<String> positionList, List<String> skillList, List<TermsAgreementItem> termsAgreementItemList) {

    public AgreeTermsCommand toAgreeTermsCommand(String userGuid) {
        return new AgreeTermsCommand(userGuid, this.termsAgreementItemList);
    }

}
