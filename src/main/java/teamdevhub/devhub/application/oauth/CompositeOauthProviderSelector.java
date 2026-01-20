package teamdevhub.devhub.application.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.application.exception.BusinessRuleException;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.port.out.auth.OauthClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompositeOauthProviderSelector implements OauthProviderSelector {

    private final List<OauthClient> oauthClientList;

    @Override
    public OauthClient select(VerificationProvider verificationProvider) {
        return oauthClientList.stream()
                .filter(oauthClient -> oauthClient.supports(verificationProvider))
                .findFirst()
                .orElseThrow(() -> BusinessRuleException.of(ErrorCode.OAUTH_FAIL));
    }
}
