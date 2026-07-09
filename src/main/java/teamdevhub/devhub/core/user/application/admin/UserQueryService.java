package teamdevhub.devhub.core.user.application.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.user.port.in.usecase.UserQueryUseCase;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.port.in.command.SearchUserCommand;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.user.port.out.UserQueryRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserQueryService implements UserQueryUseCase {

    private final UserQueryRepository userQueryRepository;

    @Override
    public PageResult<User> listUser(SearchUserCommand searchUserCommand, PageCommand pageCommand) {
        return userQueryRepository.listUser(searchUserCommand, pageCommand);
    }
}
