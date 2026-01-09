package teamdevhub.devhub.small.service.admin.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import teamdevhub.devhub.port.in.admin.command.SearchUserCommand;
import teamdevhub.devhub.adapter.in.admin.user.dto.AdminUserSummaryResponseDto;
import teamdevhub.devhub.port.in.common.command.PageCommand;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.service.admin.user.AdminUserService;
import teamdevhub.devhub.small.common.mock.repository.FakeUserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AdminUserServiceTest {

    private AdminUserService adminUserService;
    private FakeUserRepository fakeUserRepository;

    @BeforeEach
    void init() {
        fakeUserRepository = new FakeUserRepository();
        adminUserService = new AdminUserService(fakeUserRepository);
    }

    @Test
    void 관리자는_사용자_목록을_조회할_수_있다() {
        // given
        User user1 = User.createGeneralUser(
                "GUID1",
                "user1@example.com",
                "test1234",
                "User1",
                "Intro",
                List.of("001"),
                List.of("001")
        );

        User user2 = User.createGeneralUser(
                "GUID2",
                "user2@example.com",
                "test4567",
                "User2",
                "Intro",
                List.of("001"),
                List.of("001")
        );
        fakeUserRepository.saveNewUser(user1);
        fakeUserRepository.saveNewUser(user2);

        SearchUserCommand searchUserCommand = SearchUserCommand.builder()
                .blocked(null)
                .keyword(null)
                .joinedTo(null)
                .joinedFrom(null)
                .build();

        PageCommand pageCommand = PageCommand.of(0,10);


        // when
        Page<AdminUserSummaryResponseDto> adminUserSummaryResponseDtoList = adminUserService.listUser(searchUserCommand, pageCommand);
        AdminUserSummaryResponseDto getFirstAdminUserSummaryResponseDto = adminUserSummaryResponseDtoList.getContent().get(0);

        // then
        assertThat(adminUserSummaryResponseDtoList).isNotNull();
        assertThat(adminUserSummaryResponseDtoList.getContent().size()).isEqualTo(2);
        assertThat(adminUserSummaryResponseDtoList.getTotalPages()).isEqualTo(1);
        assertThat(getFirstAdminUserSummaryResponseDto.getUserGuid()).isEqualTo(user1.getUserGuid());
        assertThat(getFirstAdminUserSummaryResponseDto.getEmail()).isEqualTo(user1.getEmail());
    }
}