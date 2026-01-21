package teamdevhub.devhub.medium.adapter.out.verification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.adapter.out.exception.AdapterDataException;
import teamdevhub.devhub.adapter.out.infrastructure.persistence.verification.JpaVerificationRepository;
import teamdevhub.devhub.adapter.out.verification.VerificationAdapter;
import teamdevhub.devhub.adapter.out.verification.mapper.VerificationMapper;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.domain.verification.Verification;
import teamdevhub.devhub.domain.verification.vo.VerificationMessage;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.domain.verification.vo.VerificationType;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class VerificationAdapterMediumTest {

    @Autowired
    private VerificationAdapter verificationAdapter;

    @Autowired
    private JpaVerificationRepository jpaVerificationRepository;

    @BeforeEach
    void init() {
        jpaVerificationRepository.deleteAll();
    }

    @Test
    @DisplayName("인증정보를_저장하면_DB에_저장된다")
    void saveVerification() {
        // given
        VerificationTarget verificationTarget = new VerificationTarget(VerificationType.EMAIL, "test@email.com");
        VerificationMessage verificationMessage = new VerificationMessage("123456",LocalDateTime.now().plusMinutes(5));
        Verification verification = Verification.issue(verificationTarget, verificationMessage);

        // when
        verificationAdapter.save(verification);

        // then
        assertThat(
                jpaVerificationRepository
                        .findByVerificationTypeAndTargetValue(
                                VerificationType.EMAIL,
                                "test@email.com"
                        )
        ).isPresent();
    }

    @Test
    @DisplayName("인증대상으로_인증정보를_조회한다")
    void findByVerificationTarget() {
        // given
        VerificationTarget verificationTarget = new VerificationTarget(VerificationType.EMAIL, "test@email.com");
        VerificationMessage verificationMessage = new VerificationMessage("654321",LocalDateTime.now().plusMinutes(5));
        Verification verification = Verification.issue(verificationTarget, verificationMessage);

        jpaVerificationRepository.save(VerificationMapper.toEntity(verification));

        // when
        Verification found = verificationAdapter.findByVerificationTarget(verificationTarget);

        // then
        assertThat(found).isNotNull();
        assertThat(found.getVerificationTarget()).isEqualTo(verificationTarget);
        assertThat(found.getCode()).isEqualTo("654321");
    }

    @Test
    @DisplayName("인증정보가_없으면_예외를_발생시킨다")
    void findByVerificationTarget_notExists_throwsException() {
        // given
        VerificationTarget verificationTarget = new VerificationTarget(VerificationType.EMAIL, "notfound@email.com");

        // when then
        assertThatThrownBy(
                () -> verificationAdapter.findByVerificationTarget(verificationTarget))
                .isInstanceOf(AdapterDataException.class)
                .hasMessageContaining(ErrorCode.VERIFICATION_NOT_EXISTED.getMessage());
    }

    @Test
    @DisplayName("인증대상으로_인증정보를_삭제한다")
    void deleteByVerificationTarget() {
        // given
        VerificationTarget verificationTarget = new VerificationTarget(VerificationType.EMAIL, "delete@email.com");
        VerificationMessage verificationMessage = new VerificationMessage("000000",LocalDateTime.now().plusMinutes(5));
        Verification verification = Verification.issue(verificationTarget, verificationMessage);

        jpaVerificationRepository.save(VerificationMapper.toEntity(verification));

        // when
        verificationAdapter.deleteByVerificationTarget(verificationTarget);

        // then
        assertThat(jpaVerificationRepository.findByVerificationTypeAndTargetValue(VerificationType.EMAIL, "delete@email.com")).isEmpty();
    }
}
