package teamdevhub.devhub.port.in.admin.user;

import org.springframework.data.domain.Page;
import teamdevhub.devhub.adapter.in.admin.user.command.SearchUserCommand;
import teamdevhub.devhub.adapter.in.common.pagination.PageCommand;
import teamdevhub.devhub.domain.user.User;

public interface AdminUserUseCase {
    Page<User> listUser(SearchUserCommand searchUserCommand, PageCommand pageCommand);
}
