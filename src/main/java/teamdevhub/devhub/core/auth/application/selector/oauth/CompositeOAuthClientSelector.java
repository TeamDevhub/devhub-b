package teamdevhub.devhub.core.auth.application.selector.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.shared.enums.VerificationProvider;
import teamdevhub.devhub.core.auth.port.out.oauth.OAuthClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompositeOAuthClientSelector implements OAuthClientSelector {

    private final List<OAuthClient> oAuthClientList;

    @Override
    public OAuthClient select(VerificationProvider verificationProvider) {
        return oAuthClientList.stream()
                .filter(oAuthClient -> oAuthClient.supports(verificationProvider))
                .findFirst()
                .orElseThrow(() -> BusinessRuleException.of(ErrorCode.OAUTH_FAIL));
    }
}
