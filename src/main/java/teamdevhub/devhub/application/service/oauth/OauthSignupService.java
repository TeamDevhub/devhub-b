package teamdevhub.devhub.application.service.oauth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.domain.auth.vo.token.TempTokenInfo;
import teamdevhub.devhub.domain.auth.vo.user.OauthUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.vo.position.UserPosition;
import teamdevhub.devhub.domain.user.vo.skill.UserSkill;
import teamdevhub.devhub.port.in.oauth.usecase.OauthLoginUseCase;
import teamdevhub.devhub.port.in.oauth.command.OauthSignupCommand;
import teamdevhub.devhub.port.in.oauth.usecase.OauthSignupUseCase;
import teamdevhub.devhub.port.out.provider.IdentifierProvider;
import teamdevhub.devhub.port.out.provider.PasswordPolicyProvider;
import teamdevhub.devhub.port.out.provider.TokenIssueProvider;
import teamdevhub.devhub.port.out.user.UserPositionRepository;
import teamdevhub.devhub.port.out.user.UserRepository;
import teamdevhub.devhub.port.out.user.UserSkillRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OauthSignupService implements OauthSignupUseCase {

    private final PasswordPolicyProvider passwordPolicyProvider;
    private final IdentifierProvider identifierProvider;
    private final TokenIssueProvider tokenIssueProvider;
    private final OauthLoginUseCase oauthLoginUseCase;
    private final UserRepository userRepository;
    private final UserPositionRepository userPositionRepository;
    private final UserSkillRepository userSkillRepository;

    @Override
    public String signupWithOauth(OauthSignupCommand oauthSignupCommand) {
        TempTokenInfo tempTokenInfo = tokenIssueProvider.getTempTokenInfo(oauthSignupCommand.tempToken());
        OauthUser oauthUser = new OauthUser(tempTokenInfo.oauthId(), tempTokenInfo.verificationProvider(), tempTokenInfo.email());

        User user = createOauthUserForSignup(oauthSignupCommand, oauthUser);
        saveUserPositions(user.getUserGuid(), oauthSignupCommand.positionList());
        saveUserSkills(user.getUserGuid(), oauthSignupCommand.skillList());
        userRepository.save(user);
        return oauthSignupCommand.tempToken();
    }

    private User createOauthUserForSignup(OauthSignupCommand oauthSignupCommand, OauthUser oauthUser) {
        String userGuid = identifierProvider.generateIdentifier();
        String encodedPassword = passwordPolicyProvider.encode(oauthSignupCommand.password());
        return User.createOauthUser(
                userGuid,
                oauthUser.verificationProvider(),
                oauthUser.oauthId(),
                oauthUser.email(),
                encodedPassword,
                oauthSignupCommand.username(),
                oauthSignupCommand.introduction());
    }

    private void saveUserPositions(String userGuid, List<String> positionList) {
        Set<UserPosition> positions = positionList.stream()
                .map(position -> new UserPosition(userGuid, position))
                .collect(Collectors.toUnmodifiableSet());
        userPositionRepository.saveAll(positions);
    }

    private void saveUserSkills(String userGuid, List<String> skillList) {
        Set<UserSkill> skills = skillList.stream()
                .map(skill -> new UserSkill(userGuid, skill))
                .collect(Collectors.toUnmodifiableSet());
        userSkillRepository.saveAll(skills);
    }
}
