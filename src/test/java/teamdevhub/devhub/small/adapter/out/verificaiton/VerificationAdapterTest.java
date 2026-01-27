package teamdevhub.devhub.small.adapter.out.verificaiton;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.out.common.exception.AdapterDataException;
import teamdevhub.devhub.adapter.out.verification.VerificationAdapter;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.domain.verification.Verification;
import teamdevhub.devhub.domain.verification.vo.VerificationMessage;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.domain.verification.vo.VerificationType;
import teamdevhub.devhub.fake.framework.persistence.verificaiton.FakeJpaVerificationRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class VerificationAdapterTest {

    private VerificationAdapter verificationAdapter;
    private FakeJpaVerificationRepository fakeJpaVerificationRepository;

    @BeforeEach
    void init() {
        fakeJpaVerificationRepository = new FakeJpaVerificationRepository();
        verificationAdapter = new VerificationAdapter(fakeJpaVerificationRepository);
    }

    @Test
    @DisplayName("Verification_을_저장하면_FakeJpaRepository_에_존재한다")
    void saveVerificationStoresEntity() {
        // given
        VerificationTarget verificationTarget = new VerificationTarget(VerificationType.EMAIL, TEST_EMAIL_1);
        VerificationMessage verificationMessage = new VerificationMessage(TEST_EMAIL_CODE, LocalDateTime.now().plusHours(1));
        Verification verification = Verification.issue(verificationTarget,verificationMessage);

        // when
        verificationAdapter.save(verification);

        // then
        assertThat(fakeJpaVerificationRepository.exists(VerificationType.EMAIL, TEST_EMAIL_1)).isTrue();
    }

    @Test
    @DisplayName("VerificationTarget_으로_조회하면_도메인_Verification_을_얻는다")
    void findByVerificationTargetReturnsDomain() {
        // given
        VerificationTarget verificationTarget = new VerificationTarget(VerificationType.EMAIL, TEST_EMAIL_1);
        VerificationMessage verificationMessage = new VerificationMessage(TEST_EMAIL_CODE, LocalDateTime.now().plusHours(1));
        Verification verification = Verification.issue(verificationTarget,verificationMessage);
        verificationAdapter.save(verification);

        // when
        Verification found = verificationAdapter.findByVerificationTarget(VerificationTarget.of(VerificationType.EMAIL, TEST_EMAIL_1));

        // then
        assertThat(found.getVerificationTarget().value()).isEqualTo(TEST_EMAIL_1);
        assertThat(found.getCode()).isEqualTo(TEST_EMAIL_CODE);
    }

    @Test
    @DisplayName("존재하지 않는 VerificationTarget_으로_조회하면_예외가_발생한다")
    void findByNonExistentVerificationTargetThrows() {
        // when, then
        assertThatThrownBy(
                () -> verificationAdapter.findByVerificationTarget(
                VerificationTarget.of(VerificationType.EMAIL, "not_exist@test.com")))
                .isInstanceOf(AdapterDataException.class)
                .hasMessageContaining(ErrorCode.VERIFICATION_NOT_EXISTED.getMessage());
    }

    @Test
    @DisplayName("VerificationTarget_으로_삭제하면_FakeJpaRepository_에서_사라진다")
    void deleteByVerificationTargetRemovesEntity() {
        // given
        VerificationTarget verificationTarget = new VerificationTarget(VerificationType.EMAIL, TEST_EMAIL_1);
        VerificationMessage verificationMessage = new VerificationMessage(TEST_EMAIL_CODE, LocalDateTime.now().plusHours(1));
        Verification verification = Verification.issue(verificationTarget,verificationMessage);
        verificationAdapter.save(verification);
        assertThat(fakeJpaVerificationRepository.exists(VerificationType.EMAIL, TEST_EMAIL_1)).isTrue();

        // when
        verificationAdapter.deleteByVerificationTarget(VerificationTarget.of(VerificationType.EMAIL, TEST_EMAIL_1));

        // then
        assertThat(fakeJpaVerificationRepository.exists(VerificationType.EMAIL, TEST_EMAIL_1)).isFalse();
    }
}
