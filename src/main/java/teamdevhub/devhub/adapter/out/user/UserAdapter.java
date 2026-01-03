package teamdevhub.devhub.adapter.out.user;

import teamdevhub.devhub.adapter.out.user.entity.UserEntity;
import teamdevhub.devhub.adapter.out.user.entity.UserPositionEntity;
import teamdevhub.devhub.adapter.out.user.entity.UserSkillEntity;
import teamdevhub.devhub.adapter.out.user.mapper.UserMapper;
import teamdevhub.devhub.adapter.out.user.persistence.JpaUserPositionRepository;
import teamdevhub.devhub.adapter.out.user.persistence.JpaUserRepository;
import teamdevhub.devhub.adapter.out.user.persistence.JpaUserSkillRepository;
import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import teamdevhub.devhub.common.exception.BusinessRuleException;
import teamdevhub.devhub.domain.record.auth.LoginUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.port.out.common.IdentifierProvider;
import teamdevhub.devhub.port.out.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserAdapter implements UserRepository {

    private final JpaUserRepository jpaUserRepository;
    private final JpaUserPositionRepository jpaUserPositionRepository;
    private final JpaUserSkillRepository jpaUserSkillRepository;
    private final IdentifierProvider identifierProvider;

    @Override
    public User saveNewUser(User user) {
        UserEntity savedUser  = jpaUserRepository.save(UserMapper.toEntity(user));
        persistUserRelations(user);
        return UserMapper.toDomain(savedUser, user.getPositionList(), user.getSkillList());
    }

    @Override
    public LoginUser findForLoginByEmail(String email) {
        UserEntity userEntity = jpaUserRepository.findByEmail(email).orElseThrow(() -> BusinessRuleException.of(ErrorCodeEnum.USER_NOT_FOUND));
        return UserMapper.toLoginUser(userEntity);
    }

    @Override
    public void updateLastLoginDateTime(String userGuid, LocalDateTime lastLoginDateTime) {
        jpaUserRepository.updateLastLoginDateTime(userGuid, lastLoginDateTime);
    }

    @Override
    public User findByUserGuid(String userGuid) {
        UserEntity userEntity = jpaUserRepository.findByUserGuid(userGuid).orElseThrow(() -> BusinessRuleException.of(ErrorCodeEnum.USER_NOT_FOUND));

        List<String> positionList = jpaUserPositionRepository.findByUserGuid(userGuid)
                .stream()
                .map(UserPositionEntity::getPositionCd)
                .toList();

        List<String> skillList = jpaUserSkillRepository.findByUserGuid(userGuid)
                .stream()
                .map(UserSkillEntity::getSkillCd)
                .toList();

        return UserMapper.toDomain(userEntity, positionList, skillList);
    }

    @Override
    public User findByEmail(String email) {
        UserEntity userEntity = jpaUserRepository.findByEmail(email).orElseThrow(() -> BusinessRuleException.of(ErrorCodeEnum.USER_NOT_FOUND));
        String userGuid = userEntity.getUserGuid();

        List<String> positionList =
                jpaUserPositionRepository.findByUserGuid(userGuid)
                        .stream()
                        .map(UserPositionEntity::getPositionCd)
                        .toList();

        List<String> skillList =
                jpaUserSkillRepository.findByUserGuid(userGuid)
                        .stream()
                        .map(UserSkillEntity::getSkillCd)
                        .toList();

        return UserMapper.toDomain(userEntity, positionList, skillList);
    }

    @Override
    public boolean existsByUserRole(UserRole userRole) {
        return jpaUserRepository.existsByUserRole(userRole);
    }

    private void persistUserRelations(User user) {
        saveUserPositionList(user.getUserGuid(), user.getPositionList());
        saveUserSkillList(user.getUserGuid(), user.getSkillList());
    }

    private void saveUserPositionList(String userGuid, List<String> positionList) {
        List<UserPositionEntity> positionEntityList =
                positionList.stream()
                        .map(code -> UserPositionEntity.builder()
                                .userInterestPositionGuid(identifierProvider.generateIdentifier())
                                .userGuid(userGuid)
                                .positionCd(code)
                                .build())
                        .toList();

        jpaUserPositionRepository.saveAll(positionEntityList);
    }

    private void saveUserSkillList(String userGuid, List<String> skillList) {
        List<UserSkillEntity> skillEntityList =
                skillList.stream()
                        .map(code -> UserSkillEntity.builder()
                                .userSkillGuid(identifierProvider.generateIdentifier())
                                .userGuid(userGuid)
                                .skillCd(code)
                                .build())
                        .toList();

        jpaUserSkillRepository.saveAll(skillEntityList);
    }
}