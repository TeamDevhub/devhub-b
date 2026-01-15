package teamdevhub.devhub.small.service.admin.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.vo.PageResult;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.port.in.admin.command.SearchUserCommand;
import teamdevhub.devhub.port.in.common.command.PageCommand;
import teamdevhub.devhub.service.admin.user.AdminUserService;
import teamdevhub.devhub.fake.pure.repository.FakeUserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class AdminUserServiceTest {

    private AdminUserService adminUserService;
    private FakeUserRepository fakeUserRepository;

    @BeforeEach
    void init() {
        fakeUserRepository = new FakeUserRepository();
        adminUserService = new AdminUserService(fakeUserRepository);
    }

    @Test
    @DisplayName("사용자_목록을_조회할_수_있다")
    void canFetchUserList() {
        // given
        User user1 = User.createGeneralUser(
                TEST_USER_GUID_1,
                TEST_EMAIL_1,
                TEST_PASSWORD_1,
                TEST_USERNAME_1,
                TEST_INTRO_1
        );

        User user2 = User.createGeneralUser(
                TEST_USER_GUID_2,
                TEST_EMAIL_2,
                TEST_PASSWORD_2,
                TEST_USERNAME_2,
                TEST_INTRO_2
        );

        fakeUserRepository.save(user1);
        fakeUserRepository.save(user2);

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
        assertThat(user.getUserGuid()).isEqualTo(user1.getUserGuid());
        assertThat(user.getEmail()).isEqualTo(user1.getEmail());
    }
}