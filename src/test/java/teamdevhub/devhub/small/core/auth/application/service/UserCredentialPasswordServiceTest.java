package teamdevhub.devhub.small.core.auth.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.auth.application.service.UserCredentialService;
import teamdevhub.devhub.core.common.exception.DomainRuleException;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.core.user.port.in.command.UpdatePasswordCommand;
import teamdevhub.devhub.fake.pure.application.port.out.auth.FakeFullUserCredentialRepository;
import teamdevhub.devhub.fake.pure.application.port.out.auth.FakeRefreshTokenRepository;
import teamdevhub.devhub.fake.pure.application.provider.FakeAuthenticatedUserResolver;
import teamdevhub.devhub.fake.pure.application.provider.FakeEncodedPasswordProvider;
import teamdevhub.devhub.fake.pure.application.provider.FakeTokenParseProvider;
import teamdevhub.devhub.fake.pure.application.provider.FakeUuidIdentifierProvider;
import teamdevhub.devhub.shared.enums.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserCredentialPasswordServiceTest {

    private UserCredentialService userCredentialService;
    private FakeFullUserCredentialRepository credentialRepository;
    private FakeEncodedPasswordProvider encodedPasswordProvider;

    @BeforeEach
    void init() {
        encodedPasswordProvider = new FakeEncodedPasswordProvider();
        credentialRepository = new FakeFullUserCredentialRepository();

        userCredentialService = new UserCredentialService(
                new FakeTokenParseProvider(),
                new FakeUuidIdentifierProvider(TEST_USER_GUID_1),
                encodedPasswordProvider,
                new FakeAuthenticatedUserResolver(),
                credentialRepository,
                new FakeRefreshTokenRepository()
        );

        // 이메일 회원가입으로 자격증명 저장
        SignupUserCommand signupCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1).password(TEST_PASSWORD_1).username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1).positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST).verificationTarget(VERIFICATION_TARGET_1).build();
        userCredentialService.signupEmailUser(signupCommand);
    }

    @Test
    @DisplayName("관리자가_비밀번호를_초기화하면_새로운_비밀번호로_변경된다")
    void resetUserPassword_updatesPasswordToNewValue() {
        // when
        userCredentialService.resetUserPassword(TEST_USER_GUID_1, TEST_NEW_PASSWORD);

        // then — 새 비밀번호로 인코딩된 값이 저장되어야 한다
        String storedPassword = credentialRepository.findEmailCredentialByUserGuid(TEST_USER_GUID_1).getPassword();
        assertThat(storedPassword).isEqualTo(encodedPasswordProvider.encode(TEST_NEW_PASSWORD));
    }

    @Test
    @DisplayName("현재_비밀번호가_맞으면_updatePassword가_성공한다")
    void updatePassword_correctCurrentPassword_succeeds() {
        // given — currentPassword는 raw 비밀번호 (서비스가 내부에서 matches를 사용해 비교)
        UpdatePasswordCommand command = UpdatePasswordCommand.builder()
                .userGuid(TEST_USER_GUID_1)
                .currentPassword(TEST_PASSWORD_1)
                .newPassword(TEST_NEW_PASSWORD)
                .build();

        // when
        userCredentialService.updatePassword(command);

        // then
        String storedPassword = credentialRepository.findEmailCredentialByUserGuid(TEST_USER_GUID_1).getPassword();
        assertThat(storedPassword).isEqualTo(encodedPasswordProvider.encode(TEST_NEW_PASSWORD));
    }

    @Test
    @DisplayName("현재_비밀번호가_틀리면_updatePassword가_예외를_발생시킨다")
    void updatePassword_wrongCurrentPassword_throwsDomainRuleException() {
        // given
        UpdatePasswordCommand command = UpdatePasswordCommand.builder()
                .userGuid(TEST_USER_GUID_1)
                .currentPassword("wrongPassword")
                .newPassword(TEST_NEW_PASSWORD)
                .build();

        // when, then
        assertThatThrownBy(() -> userCredentialService.updatePassword(command))
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining(ErrorCode.USER_PASSWORD_FAIL.getMessage());
    }
}
