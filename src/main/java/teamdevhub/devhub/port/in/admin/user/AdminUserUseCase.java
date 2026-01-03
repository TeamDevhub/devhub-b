package teamdevhub.devhub.port.in.admin.user;

import org.springframework.data.domain.Page;
import teamdevhub.devhub.adapter.in.admin.user.command.SearchUserCommand;
import teamdevhub.devhub.adapter.in.admin.user.dto.AdminUserSummaryResponseDto;
import teamdevhub.devhub.adapter.in.common.pagination.PageCommand;

public interface AdminUserUseCase {
    Page<AdminUserSummaryResponseDto> listUser(SearchUserCommand searchUserCommand, PageCommand pageCommand);
}
