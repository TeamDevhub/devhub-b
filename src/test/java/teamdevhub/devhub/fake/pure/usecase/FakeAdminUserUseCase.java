package teamdevhub.devhub.fake.pure.usecase;

import teamdevhub.devhub.adapter.in.admin.user.dto.AdminUserSummaryResponseDto;
import teamdevhub.devhub.adapter.in.vo.PageResult;
import teamdevhub.devhub.port.in.admin.command.SearchUserCommand;
import teamdevhub.devhub.port.in.admin.user.AdminUserUseCase;
import teamdevhub.devhub.port.in.common.command.PageCommand;

import java.util.ArrayList;
import java.util.List;

import static teamdevhub.devhub.constant.UserTestConstant.*;

public class FakeAdminUserUseCase implements AdminUserUseCase {

    private final List<AdminUserSummaryResponseDto> adminUserSummaryResponseDtoList = new ArrayList<>();

    public FakeAdminUserUseCase() {
        adminUserSummaryResponseDtoList.add(
                AdminUserSummaryResponseDto.builder()
                        .userGuid(TEST_USER_GUID_1)
                        .email(TEST_EMAIL_1)
                        .username(TEST_USERNAME_1)
                        .blocked(false)
                        .build()
        );
        adminUserSummaryResponseDtoList.add(
                AdminUserSummaryResponseDto.builder()
                        .userGuid(TEST_USER_GUID_2)
                        .email(TEST_EMAIL_2)
                        .username(TEST_USERNAME_2)
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

        List<AdminUserSummaryResponseDto> content = start >= end ? List.of() : adminUserSummaryResponseDtoList.subList(start, end);

        return PageResult.of(
                content,
                page,
                size,
                totalElements
        );
    }
}
