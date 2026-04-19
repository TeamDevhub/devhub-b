package teamdevhub.devhub.outbound.auth.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.outbound.auth.adapter.entity.OAuthCredentialEntity;
import teamdevhub.devhub.outbound.auth.persistence.JpaOauthCredentialRepository;
import teamdevhub.devhub.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.shared.enums.VerificationProvider;

@Component
@RequiredArgsConstructor
public class OAuthCredentialAdapter {

    private final JpaOauthCredentialRepository jpaOauthCredentialRepository;

    public OAuthCredentialEntity findByProviderAndOauthId(VerificationProvider provider, String oauthId) {
        return jpaOauthCredentialRepository.findByProviderAndOauthId(provider, oauthId)
                .orElseThrow(() -> AdapterDataException.of(ErrorCode.USER_NOT_FOUND));
    }
}