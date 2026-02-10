package teamdevhub.devhub.core.user.port.in.usecase;

import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.port.in.command.SearchUserCommand;
import teamdevhub.devhub.core.common.page.PageCommand;

public interface UserQueryUseCase {

    PageResult<User> listUser(SearchUserCommand searchUserCommand, PageCommand pageCommand);
}
