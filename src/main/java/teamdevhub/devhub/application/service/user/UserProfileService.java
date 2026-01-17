package teamdevhub.devhub.application.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.vo.UserPosition;
import teamdevhub.devhub.domain.user.vo.UserPositionChangeResult;
import teamdevhub.devhub.domain.user.vo.UserSkill;
import teamdevhub.devhub.domain.user.vo.UserSkillChangeResult;
import teamdevhub.devhub.port.in.user.command.UpdateProfileCommand;
import teamdevhub.devhub.port.in.user.usecase.UserProfileUseCase;
import teamdevhub.devhub.port.out.user.UserPositionRepository;
import teamdevhub.devhub.port.out.user.UserRepository;
import teamdevhub.devhub.port.out.user.UserSkillRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileService implements UserProfileUseCase {

    private final UserRepository userRepository;
    private final UserPositionRepository userPositionRepository;
    private final UserSkillRepository userSkillRepository;

    @Override
    public User getCurrentUserProfile(String userGuid) {
        return getUserWithPositionsAndSkills(userGuid);
    }

    @Override
    public void updateProfile(UpdateProfileCommand updateProfileCommand) {
        User user = getUserWithPositionsAndSkills(updateProfileCommand.getUserGuid());

        if (updateProfileCommand.hasUsernameAndIntroductionChange()) {
            user.updateUsernameAndIntroduction(updateProfileCommand.getUsername(), updateProfileCommand.getIntroduction());
            userRepository.updateUserProfile(user);
        }

        if (updateProfileCommand.hasPositionsChange()) {
            replacePositions(user, updateProfileCommand.getPositions());
        }

        if (updateProfileCommand.hasSkillsChange()) {
            replaceSkills(user, updateProfileCommand.getSkills());
        }
    }

    private User getUserWithPositionsAndSkills(String userGuid) {
        User user = userRepository.findByUserGuid(userGuid);
        Set<UserPosition> userPositions = userPositionRepository.findByUserGuid(userGuid);
        Set<UserSkill> userSkills = userSkillRepository.findByUserGuid(userGuid);
        user.loadPositionsAndSkills(userPositions, userSkills);
        return user;
    }

    private void replacePositions(User user, Set<UserPosition> positions) {
        UserPositionChangeResult userPositionChangeResult = user.changePositions(positions);
        if (userPositionChangeResult.changed()) {
            userPositionRepository.replace(userPositionChangeResult.previousPositions(), userPositionChangeResult.currentPositions());
        }
    }

    private void replaceSkills(User user, Set<UserSkill> skills) {
        UserSkillChangeResult userSkillChangeResult = user.changeSkills(skills);
        if (userSkillChangeResult.changed()) {
            userSkillRepository.replace(userSkillChangeResult.previousSkills(), userSkillChangeResult.currentSkills());
        }
    }
}