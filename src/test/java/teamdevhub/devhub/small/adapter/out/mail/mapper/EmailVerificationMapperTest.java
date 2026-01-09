package teamdevhub.devhub.small.adapter.out.mail.mapper;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.out.mail.entity.EmailVerificationEntity;
import teamdevhub.devhub.adapter.out.mail.mapper.EmailVerificationMapper;
import teamdevhub.devhub.domain.mail.EmailVerification;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.small.mock.constant.TestConstant.EMAIL_CODE;
import static teamdevhub.devhub.small.mock.constant.TestConstant.TEST_EMAIL;

class EmailVerificationMapperTest {

    @Test
    void EmailVerificationEntity_를_EmailVerification_으로_변환할_수_있다() {
        // given
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(3);

        EmailVerificationEntity emailVerificationEntity = EmailVerificationEntity.builder()
                .email(TEST_EMAIL)
                .code(EMAIL_CODE)
                .expiredAt(expiredAt)
                .verified(true)
                .build();

        // when
        EmailVerification emailVerification = EmailVerificationMapper.toDomain(emailVerificationEntity);

        // then
        assertThat(emailVerification.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(emailVerification.getCode()).isEqualTo(EMAIL_CODE);
        assertThat(emailVerification.getExpiredAt()).isEqualTo(expiredAt);
        assertThat(emailVerification.isVerified()).isTrue();
    }

    @Test
    void EmailVerification_을_EmailVerificationEntity_로_변환할_수_있다() {
        // given
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(5);

        EmailVerification emailVerification = EmailVerification.from(
                TEST_EMAIL,
                EMAIL_CODE,
                expiredAt,
                false
        );

        // when
        EmailVerificationEntity emailVerificationEntity = EmailVerificationMapper.toEntity(emailVerification);

        // then
        assertThat(emailVerificationEntity.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(emailVerificationEntity.getCode()).isEqualTo(EMAIL_CODE);
        assertThat(emailVerificationEntity.getExpiredAt()).isEqualTo(expiredAt);
        assertThat(emailVerificationEntity.isVerified()).isFalse();
    }

    @Test
    void EmailVerificationEntity_와_EmailVerification_변환은_서로_역함수_관계이다() {
        // given
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(10);

        EmailVerificationEntity emailVerificationEntity = EmailVerificationEntity.builder()
                .email(TEST_EMAIL)
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