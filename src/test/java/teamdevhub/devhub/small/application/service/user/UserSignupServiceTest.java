package teamdevhub.devhub.small.application.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.application.service.user.UserSignupService;
import teamdevhub.devhub.domain.exception.DomainRuleException;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.fake.pure.provider.FakePasswordPolicyProvider;
import teamdevhub.devhub.fake.pure.provider.FakeUuidIdentifierProvider;
import teamdevhub.devhub.fake.pure.repository.user.FakeUserPositionRepository;
import teamdevhub.devhub.fake.pure.repository.user.FakeUserRepository;
import teamdevhub.devhub.fake.pure.repository.user.FakeUserSkillRepository;
import teamdevhub.devhub.fake.pure.usecase.verification.FakeVerificationUseCase;
import teamdevhub.devhub.port.in.user.command.SignupCommand;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class UserSignupServiceTest {

    private FakePasswordPolicyProvider fakePasswordPolicyProvider;
    private FakeVerificationUseCase fakeSignupVerificationUseCase;
    private FakeUserRepository fakeUserRepository;

    private UserSignupService userSignupService;

    @BeforeEach
    void init() {
        fakePasswordPolicyProvider = new FakePasswordPolicyProvider();
        FakeUuidIdentifierProvider fakeUuidIdentifierProvider = new FakeUuidIdentifierProvider(TEST_USER_GUID_1);
        fakeSignupVerificationUseCase = new FakeVerificationUseCase();
        fakeUserRepository = new FakeUserRepository();
        FakeUserPositionRepository fakeUserPositionRepository = new FakeUserPositionRepository();
        FakeUserSkillRepository fakeUserSkillRepository = new FakeUserSkillRepository();

        userSignupService = new UserSignupService(
                fakePasswordPolicyProvider,
                fakeUuidIdentifierProvider,
                fakeSignupVerificationUseCase,
                fakeUserRepository,
                fakeUserPositionRepository,
                fakeUserSkillRepository
        );
    }

    @Test
    @DisplayName("회원가입에_성공하면_인증_테이블에_해당_사용자의_인증내역이_삭제된다")
    void deleteEmailVerificationRecordWhenSuccessfulSignup() {
        // given
        SignupCommand signupCommand = new SignupCommand(TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1, TEST_POSITION_LIST, TEST_SKILL_LIST, VERIFICATION_TARGET_1);

        // when
        User savedUser = userSignupService.signup(signupCommand);

        // then
        assertThat(fakeUserRepository.save(savedUser).getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(fakeUserRepository.save(savedUser).getPassword()).isEqualTo(fakePasswordPolicyProvider.encode(TEST_PASSWORD_1));
    }

    @Test
    @DisplayName("인증이_완료되지_않은_사용자가_회원가입을_요청하면_예외를_던진다")
    void throwExceptionWhenSignupWithoutEmailVerification() {
        // given
        SignupCommand signupCommand = new SignupCommand(UNVERIFIED_EMAIL, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1, TEST_POSITION_LIST, TEST_SKILL_LIST, VERIFICATION_TARGET_2);
        fakeSignupVerificationUseCase.putUnverified(VERIFICATION_TARGET_2);

        // when
        assertThatThrownBy(
                () -> userSignupService.signup(signupCommand))
                // then
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining("인증에 실패했습니다.");
    }

    @Test
    @DisplayName("회원가입_후_로그인_하지_않은_사용자의_최종_로그인_일시는_존재하지_않는다")
    void haveNoLastLoginDateForUserWhoHasNotLoggedInAfterSignup() {
        // given
        SignupCommand signupCommand = new SignupCommand(UNVERIFIED_EMAIL, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1, TEST_POSITION_LIST, TEST_SKILL_LIST, VERIFICATION_TARGET_1);

        // when
        userSignupService.signup(signupCommand);

        // then
        assertThat(fakeUserRepository.wasCalled("updateLastLoginDateTime")).isFalse();
    }
}
