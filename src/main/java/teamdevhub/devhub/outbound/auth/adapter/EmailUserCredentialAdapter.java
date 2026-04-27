package teamdevhub.devhub.outbound.auth.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.auth.domain.EmailUserCredential;
import teamdevhub.devhub.core.auth.port.out.EmailUserCredentialRepository;
import teamdevhub.devhub.outbound.auth.adapter.entity.EmailCredentialEntity;
import teamdevhub.devhub.outbound.auth.adapter.mapper.UserCredentialMapper;
import teamdevhub.devhub.outbound.auth.persistence.JpaEmailCredentialRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EmailUserCredentialAdapter implements EmailUserCredentialRepository {

    private final JpaEmailCredentialRepository jpaEmailCredentialRepository;

    @Override
    public Optional<EmailUserCredential> findByEmail(String email) {
        return jpaEmailCredentialRepository.findByEmail(email)
                .map(UserCredentialMapper::toEmailUserCredential);
    }

    @Override
    public Optional<EmailUserCredential> findByUserGuid(String userGuid) {
        return jpaEmailCredentialRepository.findByUserGuid(userGuid)
                .map(UserCredentialMapper::toEmailUserCredential);
    }
}
