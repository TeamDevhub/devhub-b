package teamdevhub.devhub.outbound.auth.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.auth.domain.vo.OAuthCredential;
import teamdevhub.devhub.core.auth.port.out.OauthCredentialRepository;
import teamdevhub.devhub.outbound.auth.adapter.entity.OAuthCredentialEntity;
import teamdevhub.devhub.outbound.auth.adapter.mapper.OAuthCredentialMapper;
import teamdevhub.devhub.outbound.auth.persistence.JpaOauthCredentialRepository;
import teamdevhub.devhub.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.shared.enums.VerificationProvider;

@Component
@RequiredArgsConstructor
public class OAuthCredentialAdapter implements OauthCredentialRepository {

    private final JpaOauthCredentialRepository jpaOauthCredentialRepository;

    @Override
    public OAuthCredential findByProviderAndOauthId(VerificationProvider provider, String oauthId) {
        OAuthCredentialEntity oAuthCredentialEntity = jpaOauthCredentialRepository.findByProviderAndOauthId(provider, oauthId).orElseThrow(() -> AdapterDataException.of(ErrorCode.USER_NOT_FOUND));
        return OAuthCredentialMapper.toDomain(oAuthCredentialEntity);
    }

    @Override
    public OAuthCredential findByUserGuid(String userGuid) {
        OAuthCredentialEntity oAuthCredentialEntity = jpaOauthCredentialRepository.findByUserGuid(userGuid).orElseThrow(() -> AdapterDataException.of(ErrorCode.USER_NOT_FOUND));
        return OAuthCredentialMapper.toDomain(oAuthCredentialEntity);
    }
}