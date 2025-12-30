package teamdevhub.devhub.service.user;

import teamdevhub.devhub.adapter.in.user.command.SignupCommand;
import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import teamdevhub.devhub.common.exception.BusinessRuleException;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.port.in.user.UserUseCase;
import teamdevhub.devhub.port.out.common.IdentifierGeneratorPort;
import teamdevhub.devhub.port.out.common.PasswordPolicyPort;
import teamdevhub.devhub.port.out.mail.EmailCertificationPort;
import teamdevhub.devhub.port.out.user.UserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserUseCase {

    private final UserPort userPort;
    private final EmailCertificationPort emailCertificationPort;
    private final PasswordPolicyPort passwordPolicyPort;
    private final IdentifierGeneratorPort identifierGeneratorPort;

    @Override
    public User signup(SignupCommand signupCommand) {
        if (!emailCertificationPort.isVerified(signupCommand.getEmail())) {
            throw BusinessRuleException.of(ErrorCodeEnum.EMAIL_NOT_CONFIRMED);
        }
        String userGuid = identifierGeneratorPort.generate();
        String encodedPassword = passwordPolicyPort.encode(signupCommand.getPassword());
        User user = User.createGeneralUser(userGuid, signupCommand.getEmail(), signupCommand.getUsername(), encodedPassword);
        emailCertificationPort.delete(signupCommand.getEmail());
        return userPort.save(user);
    }

    @Override
    public void createAdminUser(String email, String username, String rawPassword) {
        String userGuid = identifierGeneratorPort.generate();
        String encodedPassword = passwordPolicyPort.encode(rawPassword);
        User adminUser = User.createAdminUser(userGuid, email, username, encodedPassword);
        userPort.save(adminUser);
    }

    @Override
    public boolean existsByRole(UserRole role) {
        return userPort.existsByRole(role);
    }

    @Override
    public void update() {

    }
}