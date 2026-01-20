package teamdevhub.devhub.fake.pure.usecase.admin.user;

import teamdevhub.devhub.adapter.in.common.vo.PageResult;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.port.in.admin.command.SearchUserCommand;
import teamdevhub.devhub.port.in.admin.user.AdminUserUseCase;
import teamdevhub.devhub.port.in.common.command.PageCommand;

import java.util.ArrayList;
import java.util.List;

import static teamdevhub.devhub.constant.UserTestConstant.*;

public class FakeAdminUserUseCase implements AdminUserUseCase {

    private final List<User> userList = new ArrayList<>();

    public FakeAdminUserUseCase() {
        userList.add(
                User.builder()
                        .userGuid(TEST_USER_GUID_1)
                        .password(TEST_PASSWORD_1)
                        .email(TEST_EMAIL_1)
                        .username(TEST_USERNAME_1)
                        .blocked(false)
                        .build()
        );
        userList.add(
                User.builder()
                        .userGuid(TEST_USER_GUID_2)
                        .password(TEST_PASSWORD_2)
                        .email(TEST_EMAIL_2)
                        .username(TEST_USERNAME_2)
                        .blocked(false)
                        .build()
        );
    }

    @Override
    public PageResult<User> listUser(SearchUserCommand searchUserCommand, PageCommand pageCommand) {
        int page = pageCommand.page();
        int size = pageCommand.size();

        long totalElements = userList.size();

        int start = page * size;
        int end = Math.min(start + size, userList.size());

        List<User> content = start >= end ? List.of() : userList.subList(start, end);

        return PageResult.of(
                content,
                page,
                size,
                totalElements
        );
    }
}
