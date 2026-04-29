package teamdevhub.devhub.core.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.domain.vo.command.UpdateUserCommand;
import teamdevhub.devhub.core.user.port.in.command.AdminUpdateUserCommand;
import teamdevhub.devhub.core.user.port.in.command.BanUserCommand;
import teamdevhub.devhub.core.user.port.in.usecase.AdminUserManagementUseCase;
import teamdevhub.devhub.core.user.port.out.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminUserManagementService implements AdminUserManagementUseCase {

    private final UserRepository userRepository;

    @Override
    public void banUser(BanUserCommand command) {
        User user = userRepository.findByUserGuid(command.userGuid());
        user.ban(command.blockEndDate());
        userRepository.save(user);
    }

    @Override
    public void unbanUser(String userGuid) {
        User user = userRepository.findByUserGuid(userGuid);
        user.unban();
        userRepository.save(user);
    }

    @Override
    public void updateUser(AdminUpdateUserCommand command) {
        User user = userRepository.findByUserGuid(command.userGuid());
        user.updateBasicProfile(new UpdateUserCommand(command.username(), command.introduction()));
        userRepository.save(user);
    }
}
