package teamdevhub.devhub.outbound.auth.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.outbound.auth.adapter.entity.EmailCredentialEntity;
import teamdevhub.devhub.outbound.auth.persistence.JpaEmailCredentialRepository;
import teamdevhub.devhub.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.shared.enums.ErrorCode;

@Component
@RequiredArgsConstructor
public class EmailCredentialAdapter {

    private final JpaEmailCredentialRepository jpaEmailCredentialRepository;

    public EmailCredentialEntity findByEmail(String email) {
        return jpaEmailCredentialRepository.findByEmail(email)
                .orElseThrow(() -> AdapterDataException.of(ErrorCode.USER_NOT_FOUND));
    }
}