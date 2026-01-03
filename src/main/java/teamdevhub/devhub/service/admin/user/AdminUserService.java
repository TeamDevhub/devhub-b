package teamdevhub.devhub.service.admin.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.adapter.in.admin.user.command.SearchUserCommand;
import teamdevhub.devhub.adapter.in.admin.user.dto.AdminUserSummaryResponseDto;
import teamdevhub.devhub.adapter.in.common.pagination.PageCommand;
import teamdevhub.devhub.port.in.admin.user.AdminUserUseCase;
import teamdevhub.devhub.port.out.user.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminUserService implements AdminUserUseCase {

    private final UserRepository userRepository;

    @Override
    public Page<AdminUserSummaryResponseDto> listUser(SearchUserCommand searchUserCommand, PageCommand pageCommand) {
        Page<AdminUserSummaryResponseDto> pagedUserList = userRepository.listUser(searchUserCommand, pageCommand);
        return new PageImpl<>(
                pagedUserList.getContent(),
                pagedUserList.getPageable(),
                pagedUserList.getTotalElements()
        );
    }
}
