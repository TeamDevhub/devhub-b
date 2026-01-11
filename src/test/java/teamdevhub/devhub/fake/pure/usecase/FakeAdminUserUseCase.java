package teamdevhub.devhub.fake.pure.usecase;

import teamdevhub.devhub.adapter.in.admin.user.dto.AdminUserSummaryResponseDto;
import teamdevhub.devhub.adapter.in.vo.PageResult;
import teamdevhub.devhub.port.in.admin.command.SearchUserCommand;
import teamdevhub.devhub.port.in.admin.user.AdminUserUseCase;
import teamdevhub.devhub.port.in.common.command.PageCommand;

import java.util.ArrayList;
import java.util.List;

public class FakeAdminUserUseCase implements AdminUserUseCase {

    private final List<AdminUserSummaryResponseDto> adminUserSummaryResponseDtoList = new ArrayList<>();

    public FakeAdminUserUseCase() {
        adminUserSummaryResponseDtoList.add(
                AdminUserSummaryResponseDto.builder()
                        .userGuid("GUID1")
                        .email("user1@example.com")
                        .username("User1")
                        .blocked(false)
                        .build()
        );
        adminUserSummaryResponseDtoList.add(
                AdminUserSummaryResponseDto.builder()
                        .userGuid("GUID2")
                        .email("user2@example.com")
                        .username("User2")
                        .blocked(false)
                        .build()
        );
    }

    @Override
    public PageResult<AdminUserSummaryResponseDto> listUser(SearchUserCommand searchUserCommand, PageCommand pageCommand) {
        int page = pageCommand.getPage();
        int size = pageCommand.getSize();

        long totalElements = adminUserSummaryResponseDtoList.size();

        int start = page * size;
        int end = Math.min(start + size, adminUserSummaryResponseDtoList.size());

        List<AdminUserSummaryResponseDto> content =
                start >= end
                        ? List.of()
                        : adminUserSummaryResponseDtoList.subList(start, end);

        return PageResult.of(
                content,
                page,
                size,
                totalElements
        );
    }
}
