package teamdevhub.devhub.small.adapter.out.mail.entity;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.out.mail.entity.EmailVerificationEntity;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.small.mock.constant.TestConstant.EMAIL_CODE;
import static teamdevhub.devhub.small.mock.constant.TestConstant.TEST_EMAIL;

class EmailVerificationEntityTest {

    @Test
    void 빌더로_EmailVerificationEntity_를_생성할_수_있다() {
        // given
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(5);

        // when
        EmailVerificationEntity emailVerificationEntity = EmailVerificationEntity.builder()
                .email(TEST_EMAIL)
                .code(EMAIL_CODE)
                .expiredAt(expiredAt)
                .verified(false)
                .build();

        // then
        assertThat(emailVerificationEntity.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(emailVerificationEntity.getCode()).isEqualTo(EMAIL_CODE);
        assertThat(emailVerificationEntity.getExpiredAt()).isEqualTo(expiredAt);
        assertThat(emailVerificationEntity.isVerified()).isFalse();
    }

    @Test
    void 모든_필드를_포함한_생성자로_객체를_생성할_수_있다() {
        // given
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(10);

        // when
        EmailVerificationEntity entity = new EmailVerificationEntity(
                TEST_EMAIL,
                EMAIL_CODE,
                expiredAt,
                true
        );

        // then
        assertThat(entity.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(entity.getCode()).isEqualTo(EMAIL_CODE);
        assertThat(entity.getExpiredAt()).isEqualTo(expiredAt);
        assertThat(entity.isVerified()).isTrue();
    }

    @Test
    void 기본_생성자가_존재한다() {
        // when
        EmailVerificationEntity emailVerificationEntity = new EmailVerificationEntity();

        // then
        assertThat(emailVerificationEntity).isNotNull();
    }

}