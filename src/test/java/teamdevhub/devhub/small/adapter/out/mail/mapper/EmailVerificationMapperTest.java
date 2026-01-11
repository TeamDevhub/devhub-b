package teamdevhub.devhub.small.adapter.out.mail.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.out.mail.entity.EmailVerificationEntity;
import teamdevhub.devhub.adapter.out.mail.mapper.EmailVerificationMapper;
import teamdevhub.devhub.domain.mail.EmailVerification;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.TestConstant.EMAIL_CODE;
import static teamdevhub.devhub.constant.TestConstant.TEST_EMAIL_1;

class EmailVerificationMapperTest {

    @Test
    @DisplayName("EmailVerificationEntity_를_EmailVerification_으로_변환할_수_있다")
    void convertEntityToDomain() {
        // given
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(3);

        EmailVerificationEntity emailVerificationEntity = EmailVerificationEntity.builder()
                .email(TEST_EMAIL_1)
                .code(EMAIL_CODE)
                .expiredAt(expiredAt)
                .verified(true)
                .build();

        // when
        EmailVerification emailVerification = EmailVerificationMapper.toDomain(emailVerificationEntity);

        // then
        assertThat(emailVerification.getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(emailVerification.getCode()).isEqualTo(EMAIL_CODE);
        assertThat(emailVerification.getExpiredAt()).isEqualTo(expiredAt);
        assertThat(emailVerification.isVerified()).isTrue();
    }

    @Test
    @DisplayName("EmailVerification_을_EmailVerificationEntity_로_변환할_수_있다")
    void convertDomainToEntity() {
        // given
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(5);

        EmailVerification emailVerification = EmailVerification.from(
                TEST_EMAIL_1,
                EMAIL_CODE,
                expiredAt,
                false
        );

        // when
        EmailVerificationEntity emailVerificationEntity = EmailVerificationMapper.toEntity(emailVerification);

        // then
        assertThat(emailVerificationEntity.getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(emailVerificationEntity.getCode()).isEqualTo(EMAIL_CODE);
        assertThat(emailVerificationEntity.getExpiredAt()).isEqualTo(expiredAt);
        assertThat(emailVerificationEntity.isVerified()).isFalse();
    }

    @Test
    @DisplayName("EmailVerificationEntity_와_EmailVerification_변환은_서로_역함수_관계이다")
    void isEmailVerificationConversionInverse() {
        // given
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(10);

        EmailVerificationEntity emailVerificationEntity = EmailVerificationEntity.builder()
                .email(TEST_EMAIL_1)
                .code(EMAIL_CODE)
                .expiredAt(expiredAt)
                .verified(true)
                .build();

        // when
        EmailVerification emailVerification = EmailVerificationMapper.toDomain(emailVerificationEntity);
        EmailVerificationEntity result = EmailVerificationMapper.toEntity(emailVerification);

        // then
        assertThat(result.getEmail()).isEqualTo(emailVerificationEntity.getEmail());
        assertThat(result.getCode()).isEqualTo(emailVerificationEntity.getCode());
        assertThat(result.getExpiredAt()).isEqualTo(emailVerificationEntity.getExpiredAt());
        assertThat(result.isVerified()).isEqualTo(emailVerificationEntity.isVerified());
    }
}