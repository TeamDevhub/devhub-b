package teamdevhub.devhub.service.admin.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.adapter.in.admin.user.dto.AdminUserSummaryResponseDto;
import teamdevhub.devhub.adapter.in.vo.PageResult;
import teamdevhub.devhub.port.in.admin.command.SearchUserCommand;
import teamdevhub.devhub.port.in.admin.user.AdminUserUseCase;
import teamdevhub.devhub.port.in.common.command.PageCommand;
import teamdevhub.devhub.port.out.user.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminUserService implements AdminUserUseCase {

    private final UserRepository userRepository;

    @Override
    public PageResult<AdminUserSummaryResponseDto> listUser(SearchUserCommand searchUserCommand, PageCommand pageCommand) {
        return userRepository.listUser(searchUserCommand, pageCommand.getPage(), pageCommand.getSize());
    }
}
