//package teamdevhub.devhub.small.application.service.user;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import teamdevhub.devhub.application.exception.BusinessRuleException;
//import teamdevhub.devhub.application.service.user.UserSignupService;
//import teamdevhub.devhub.domain.user.User;
//import teamdevhub.devhub.fake.pure.provider.FakePasswordPolicyProvider;
//import teamdevhub.devhub.fake.pure.provider.FakeUuidIdentifierProvider;
//import teamdevhub.devhub.fake.pure.repository.FakeUserPositionRepository;
//import teamdevhub.devhub.fake.pure.repository.FakeUserRepository;
//import teamdevhub.devhub.fake.pure.repository.FakeUserSkillRepository;
//import teamdevhub.devhub.fake.pure.usecase.FakeSignupVerificationUseCase;
//import teamdevhub.devhub.port.in.user.command.SignupCommand;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static teamdevhub.devhub.constant.UserTestConstant.*;
//
//public class UserSignupServiceTest {
//
//    private UserSignupService userSignupService;
//    private FakeUserRepository fakeUserRepository;
//    private FakePasswordPolicyProvider fakePasswordPolicyProvider;
//
//    @BeforeEach
//    void init() {
//        FakeSignupVerificationUseCase fakeSignupVerificationUseCase = new FakeSignupVerificationUseCase();
//
//        fakeUserRepository = new FakeUserRepository();
//        FakeUserPositionRepository fakeUserPositionRepository = new FakeUserPositionRepository();
//        FakeUserSkillRepository fakeUserSkillRepository = new FakeUserSkillRepository();
//
//        FakeUuidIdentifierProvider fakeUuidIdentifierProvider = new FakeUuidIdentifierProvider(TEST_USER_GUID_1);
//        fakePasswordPolicyProvider = new FakePasswordPolicyProvider();
//
//        userSignupService = new UserSignupService(
//                fakeSignupVerificationUseCase,
//                fakeUserRepository,
//                fakeUserPositionRepository,
//                fakeUserSkillRepository,
//                fakePasswordPolicyProvider,
//                fakeUuidIdentifierProvider
//        );
//    }
//
//
//    @Test
//    @DisplayName("회원가입에_성공하면_이메일_인증_테이블에_해당_사용자의_인증내역이_삭제된다")
//    void deleteEmailVerificationRecordWhenSuccessfulSignup() {
//        // given
//        SignupCommand signupCommand = new SignupCommand(TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1, TEST_POSITION_LIST, TEST_SKILL_LIST, VERIFICATION_TARGET);
//
//        // when
//        User savedUser = userSignupService.signup(signupCommand);
//
//        // then
//        assertThat(fakeUserRepository.save(savedUser).getUserGuid()).isEqualTo(TEST_USER_GUID_1);
//        assertThat(fakeUserRepository.save(savedUser).getPassword()).isEqualTo(fakePasswordPolicyProvider.encode(TEST_PASSWORD_1));
//    }
//
//    @Test
//    @DisplayName("이메일_인증이_완료되지_않은_사용자가_회원가입을_요청하면_예외를_던진다")
//    void throwExceptionWhenSignupWithoutEmailVerification() {
//        // given
//        SignupCommand signupCommand = new SignupCommand(UNVERIFIED_EMAIL, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1, TEST_POSITION_LIST, TEST_SKILL_LIST);
//
//        // when
//        assertThatThrownBy(
//                () -> userSignupService.signup(signupCommand))
//                // then
//                .isInstanceOf(BusinessRuleException.class)
//                .hasMessageContaining("E-mail 인증에 실패했습니다.");
//    }
//
//    @Test
//    @DisplayName("회원가입_후_로그인_하지_않은_사용자의_최종_로그인_일시는_존재하지_않는다")
//    void haveNoLastLoginDateForUserWhoHasNotLoggedInAfterSignup() {
//        // given
//        SignupCommand signupCommand = new SignupCommand(TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1, TEST_POSITION_LIST, TEST_SKILL_LIST);
//
//        // when
//        userSignupService.signup(signupCommand);
//
//        // then
//        assertThat(fakeUserRepository.wasCalled("updateLastLoginDateTime")).isFalse();
//    }
//}
