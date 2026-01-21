package teamdevhub.devhub.adapter.out.infrastructure.oauth.selector;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.application.exception.BusinessRuleException;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.port.out.oauth.OauthClient;
import teamdevhub.devhub.port.out.selector.OauthClientSelector;

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
