package teamdevhub.devhub.small.application.service.admin.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.common.vo.PageResult;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.vo.user.UserCreateCommand;
import teamdevhub.devhub.port.in.admin.command.SearchUserCommand;
import teamdevhub.devhub.port.in.common.command.PageCommand;
import teamdevhub.devhub.application.service.admin.user.AdminUserService;
import teamdevhub.devhub.fake.pure.repository.user.FakeUserRepository;
import teamdevhub.devhub.port.in.user.command.SignupUserCommand;

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
        UserCreateCommand generalUserCreateCommand1 = UserCreateCommand.generalUserCreateCommand(signupUserCommand1, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser1 = User.createGeneralUser(generalUserCreateCommand1);

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
        UserCreateCommand generalUserCreateCommand2 = UserCreateCommand.generalUserCreateCommand(signupUserCommand2, TEST_USER_GUID_2, TEST_PASSWORD_2);
        User testUser2 = User.createGeneralUser(generalUserCreateCommand2);

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