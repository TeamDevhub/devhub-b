package teamdevhub.devhub.core.user.port.in.usecase;

import teamdevhub.devhub.core.user.port.in.command.AdminUpdateUserCommand;
import teamdevhub.devhub.core.user.port.in.command.BanUserCommand;

public interface AdminUserManagementUseCase {

    void banUser(BanUserCommand command);
    void unbanUser(String userGuid);
    void updateUser(AdminUpdateUserCommand command);
}
