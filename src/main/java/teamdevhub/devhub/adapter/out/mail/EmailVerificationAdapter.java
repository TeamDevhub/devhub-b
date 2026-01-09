package teamdevhub.devhub.adapter.out.mail;

import teamdevhub.devhub.adapter.out.exception.AdapterDataException;
import teamdevhub.devhub.adapter.out.mail.entity.EmailVerificationEntity;
import teamdevhub.devhub.adapter.out.mail.mapper.EmailVerificationMapper;
import teamdevhub.devhub.adapter.out.mail.persistence.JpaEmailVerificationRepository;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.common.provider.datetime.DateTimeProvider;
import teamdevhub.devhub.domain.mail.EmailVerification;
import teamdevhub.devhub.port.out.mail.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class EmailVerificationAdapter implements EmailVerificationRepository {

    private final JpaEmailVerificationRepository jpaEmailVerificationRepository;
    private final DateTimeProvider dateTimeProvider;

    @Override
    public EmailVerification findByEmail(String email) {
        return jpaEmailVerificationRepository.findById(email)
                .map(EmailVerificationMapper::toDomain)
                .orElseThrow(() -> AdapterDataException.of(ErrorCode.READ_FAIL));
    }

    @Override
    public boolean existUnexpiredCode(String email) {
        return jpaEmailVerificationRepository.findById(email)
                .filter(emailVerificationEntity -> !emailVerificationEntity.getExpiredAt().isBefore(dateTimeProvider.now()))
                .isPresent();
    }

    @Override
    public void save(EmailVerification emailVerification) {
        EmailVerificationEntity emailVerificationEntity = EmailVerificationMapper.toEntity(emailVerification);
        jpaEmailVerificationRepository.save(emailVerificationEntity);
    }

    @Override
    public void delete(String email) {
        jpaEmailVerificationRepository.deleteById(email);
    }
}