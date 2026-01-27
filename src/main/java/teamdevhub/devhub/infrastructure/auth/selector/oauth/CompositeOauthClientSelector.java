package teamdevhub.devhub.infrastructure.auth.selector.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.auth.application.selector.OauthClientSelector;
import teamdevhub.devhub.shared.exception.BusinessRuleException;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.shared.enums.VerificationProvider;
import teamdevhub.devhub.core.auth.port.out.OauthClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompositeOauthClientSelector implements OauthClientSelector {

    private final List<OauthClient> oauthClientList;

    @Override
    public OauthClient select(VerificationProvider verificationProvider) {
        return oauthClientList.stream()
                .filter(oauthClient -> oauthClient.supports(verificationProvider))
                .findFirst()
                .orElseThrow(() -> BusinessRuleException.of(ErrorCode.OAUTH_FAIL));
    }
}
