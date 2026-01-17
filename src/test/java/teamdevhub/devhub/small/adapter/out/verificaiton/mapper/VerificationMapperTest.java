package teamdevhub.devhub.small.adapter.out.verificaiton.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.out.verification.entity.VerificationEntity;
import teamdevhub.devhub.adapter.out.verification.mapper.VerificationMapper;
import teamdevhub.devhub.domain.verification.Verification;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.domain.verification.vo.VerificationType;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_EMAIL_1;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_EMAIL_CODE;

public class VerificationMapperTest {

    @Test
    @DisplayName("Verification_을_VerificationEntity_로_올바르게_변환한다")
    void convertsDomainToEntity() {
        // given
        Verification verification = Verification.issue(VerificationTarget.of(VerificationType.EMAIL, TEST_EMAIL_1), TEST_EMAIL_CODE, LocalDateTime.now().plusHours(1));

        // when
        VerificationEntity verificationEntity = VerificationMapper.toEntity(verification);

        // then
        assertThat(verificationEntity.getVerificationType()).isEqualTo(verification.getVerificationTarget().verificationType());
        assertThat(verificationEntity.getTargetValue()).isEqualTo(verification.getVerificationTarget().value());
        assertThat(verificationEntity.getCode()).isEqualTo(verification.getCode());
        assertThat(verificationEntity.getExpiredAt()).isEqualTo(verification.getExpiredAt());
        assertThat(verificationEntity.isVerified()).isEqualTo(verification.isVerified());
    }

    @Test
    @DisplayName("VerificationEntity_를_도메인_Verification_으로_올바르게_변환한다")
    void convertsEntityToDomain() {
        // given
        VerificationEntity verificationEntity = VerificationEntity.builder()
                .id(1L)
                .verificationType(VerificationType.EMAIL)
                .targetValue(TEST_EMAIL_1)
                .code(TEST_EMAIL_CODE)
                .expiredAt(LocalDateTime.now().plusHours(1))
                .verified(false)
                .build();

        // when
        Verification verification = VerificationMapper.toDomain(verificationEntity);

        // then
        assertThat(verification.getId()).isEqualTo(verificationEntity.getId());
        assertThat(verification.getVerificationTarget().verificationType()).isEqualTo(verificationEntity.getVerificationType());
        assertThat(verification.getVerificationTarget().value()).isEqualTo(verificationEntity.getTargetValue());
        assertThat(verification.getCode()).isEqualTo(verificationEntity.getCode());
        assertThat(verification.getExpiredAt()).isEqualTo(verificationEntity.getExpiredAt());
        assertThat(verification.isVerified()).isEqualTo(verificationEntity.isVerified());
    }
}
