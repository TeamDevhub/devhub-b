package teamdevhub.devhub.outbound.auth.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.auth.domain.vo.EmailCredential;
import teamdevhub.devhub.core.auth.port.out.EmailCredentialRepository;
import teamdevhub.devhub.outbound.auth.adapter.entity.EmailCredentialEntity;
import teamdevhub.devhub.outbound.auth.adapter.mapper.EmailCredentialMapper;
import teamdevhub.devhub.outbound.auth.persistence.JpaEmailCredentialRepository;
import teamdevhub.devhub.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.shared.enums.ErrorCode;

@Component
@RequiredArgsConstructor
public class EmailCredentialAdapter implements EmailCredentialRepository {

    private final JpaEmailCredentialRepository jpaEmailCredentialRepository;

    @Override
    public EmailCredential findByEmail(String email) {
        EmailCredentialEntity emailCredentialEntity = jpaEmailCredentialRepository.findByEmail(email).orElseThrow(() -> AdapterDataException.of(ErrorCode.USER_NOT_FOUND));
        return EmailCredentialMapper.toDomain(emailCredentialEntity);
    }

    @Override
    public EmailCredential findByUserGuid(String userGuid) {
        EmailCredentialEntity emailCredentialEntity = jpaEmailCredentialRepository.findByUserGuid(userGuid).orElseThrow(() -> AdapterDataException.of(ErrorCode.USER_NOT_FOUND));
        return EmailCredentialMapper.toDomain(emailCredentialEntity);
    }
}