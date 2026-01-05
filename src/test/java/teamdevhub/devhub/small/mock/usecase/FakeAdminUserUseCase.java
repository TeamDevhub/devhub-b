package teamdevhub.devhub.small.mock.usecase;

import org.springframework.data.domain.*;
import teamdevhub.devhub.adapter.in.admin.user.command.SearchUserCommand;
import teamdevhub.devhub.adapter.in.admin.user.dto.AdminUserSummaryResponseDto;
import teamdevhub.devhub.adapter.in.common.pagination.PageCommand;
import teamdevhub.devhub.adapter.out.user.mapper.UserMapper;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.port.in.admin.user.AdminUserUseCase;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public Page<AdminUserSummaryResponseDto> listUser(SearchUserCommand searchUserCommand, PageCommand pageCommand) {
        Pageable pageable = PageRequest.of(
                pageCommand.getPage(),
                pageCommand.getSize(),
                Sort.by("regDt").descending()
        );

        List<AdminUserSummaryResponseDto> content =
                adminUserSummaryResponseDtoList.stream()
                        .skip(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .toList();

        return new PageImpl<>(
                content,
                pageable,
                adminUserSummaryResponseDtoList.size()
        );
    }
}
