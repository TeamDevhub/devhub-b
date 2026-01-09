package teamdevhub.devhub.small.adapter.out.mail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.out.exception.AdapterDataException;
import teamdevhub.devhub.adapter.out.mail.EmailVerificationAdapter;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.domain.mail.EmailVerification;
import teamdevhub.devhub.small.mock.persistence.mail.FakeJpaEmailVerificationRepository;
import teamdevhub.devhub.small.mock.provider.FakeDateTimeProvider;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.small.mock.constant.TestConstant.*;

class EmailVerificationAdapterTest {

    private EmailVerificationAdapter emailVerificationAdapter;
    private FakeJpaEmailVerificationRepository fakeJpaEmailVerificationRepository;
    private FakeDateTimeProvider fakeDateTimeProvider;

    @BeforeEach
    void init() {
        fakeJpaEmailVerificationRepository = new FakeJpaEmailVerificationRepository();
        fakeDateTimeProvider = new FakeDateTimeProvider(LocalDateTime.of(2025, 1, 1, 12, 0));
        emailVerificationAdapter = new EmailVerificationAdapter(fakeJpaEmailVerificationRepository, fakeDateTimeProvider);
    }

    @Test
    void 이메일_인증_내역을_조회할_수_있다() {
        // given
        EmailVerification emailVerification = EmailVerification.issue(TEST_EMAIL, EMAIL_CODE, fakeDateTimeProvider.now().plusMinutes(5));
        emailVerificationAdapter.save(emailVerification);

        // when
        EmailVerification foundEmailVerification = emailVerificationAdapter.findByEmail(TEST_EMAIL);

        // then
        assertThat(foundEmailVerification).isNotNull();
        assertThat(foundEmailVerification.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(foundEmailVerification.getCode()).isEqualTo(EMAIL_CODE);
    }

    @Test
    void 만료되지_않은_코드는_existUnexpiredCode_에서_true_를_반환한다() {
        // given
        EmailVerification emailVerification = EmailVerification.issue(TEST_EMAIL, EMAIL_CODE, fakeDateTimeProvider.now().plusMinutes(5));
        emailVerificationAdapter.save(emailVerification);

        // when
        boolean exists = emailVerificationAdapter.existUnexpiredCode(TEST_EMAIL);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void 만료된_코드는_existUnexpiredCode_에서_false_를_반환한다() {
        // given
        EmailVerification emailVerification = EmailVerification.issue(TEST_EMAIL, EMAIL_CODE, fakeDateTimeProvider.now().minusMinutes(1));
        emailVerificationAdapter.save(emailVerification);

        // when
        boolean exists = emailVerificationAdapter.existUnexpiredCode(TEST_EMAIL);

        // then
        assertThat(exists).isFalse();
    }

    @Test
    void 저장된_이메일_인증_내역을_삭제할_수_있다() {
        // given
        EmailVerification emailVerification = EmailVerification.issue(TEST_EMAIL, EMAIL_CODE, fakeDateTimeProvider.now().plusMinutes(5));
        emailVerificationAdapter.save(emailVerification);

        // when
        emailVerificationAdapter.delete(TEST_EMAIL);

        // then
        assertThat(emailVerificationAdapter.existUnexpiredCode(TEST_EMAIL)).isFalse();
        assertThatThrownBy(() -> emailVerificationAdapter.findByEmail(TEST_EMAIL))
                .isInstanceOf(AdapterDataException.class)
                .hasMessageContaining(ErrorCode.READ_FAIL.getMessage());
    }
}