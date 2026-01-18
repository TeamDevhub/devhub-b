package teamdevhub.devhub.port.in.admin.user;

import teamdevhub.devhub.adapter.in.vo.PageResult;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.port.in.admin.command.SearchUserCommand;
import teamdevhub.devhub.port.in.common.command.PageCommand;

public interface AdminUserUseCase {

    PageResult<User> listUser(SearchUserCommand searchUserCommand, PageCommand pageCommand);
}
