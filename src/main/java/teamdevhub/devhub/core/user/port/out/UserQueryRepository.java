package teamdevhub.devhub.core.user.port.out;

import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.port.in.command.SearchUserCommand;

public interface UserQueryRepository {

    PageResult<User> listUser(SearchUserCommand searchUserCommand, PageCommand pageCommand);
}
