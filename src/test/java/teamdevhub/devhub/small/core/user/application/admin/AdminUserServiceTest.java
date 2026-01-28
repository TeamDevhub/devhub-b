package teamdevhub.devhub.small.core.user.application.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.domain.vo.user.CreateUserCommand;
import teamdevhub.devhub.core.user.port.in.command.SearchUserCommand;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.user.application.admin.AdminUserService;
import teamdevhub.devhub.fake.pure.application.port.out.user.FakeUserRepository;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class AdminUserServiceTest {

    private AdminUserService adminUserService;

    private FakeUserRepository userRepository;

    @BeforeEach
    void init() {
        userRepository = new FakeUserRepository();

        adminUserService = new AdminUserService(userRepository);
    }

    @Test
    @DisplayName("사용자_목록을_조회할_수_있다")
    void canFetchUserList() {
        // given
        SignupUserCommand signupUserCommand1 = SignupUserCommand.builder()
                .userGuid(null)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand1 = CreateUserCommand.generalUserCreateCommand(signupUserCommand1, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser1 = User.createGeneralUser(generalCreateUserCommand1);

        SignupUserCommand signupUserCommand2 = SignupUserCommand.builder()
                .userGuid(null)
                .email(TEST_EMAIL_2)
                .password(TEST_PASSWORD_2)
                .username(TEST_USERNAME_2)
                .introduction(TEST_INTRO_2)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_2)
                .build();
        CreateUserCommand generalCreateUserCommand2 = CreateUserCommand.generalUserCreateCommand(signupUserCommand2, TEST_USER_GUID_2, TEST_PASSWORD_2);
        User testUser2 = User.createGeneralUser(generalCreateUserCommand2);

        userRepository.save(testUser1);
        userRepository.save(testUser2);

        SearchUserCommand searchUserCommand = SearchUserCommand.builder()
                .blocked(null)
                .keyword(null)
                .joinedTo(null)
                .joinedFrom(null)
                .build();

        PageCommand pageCommand = PageCommand.of(0,10);

        // when
        PageResult<User> pagedUserList = adminUserService.listUser(searchUserCommand, pageCommand);
        User user = pagedUserList.content().get(0);

        // then
        assertThat(pagedUserList).isNotNull();
        assertThat(pagedUserList.content().size()).isEqualTo(2);
        assertThat(pagedUserList.totalPages()).isEqualTo(1);
        assertThat(user.getUserGuid()).isEqualTo(testUser1.getUserGuid());
        assertThat(user.getEmail()).isEqualTo(testUser1.getEmail());
    }
}