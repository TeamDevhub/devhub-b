package teamdevhub.devhub.adapter.out.user;

import teamdevhub.devhub.adapter.out.common.entity.RelationDiff;
import teamdevhub.devhub.adapter.out.common.util.RelationDiffUtil;
import teamdevhub.devhub.adapter.out.user.entity.UserEntity;
import teamdevhub.devhub.adapter.out.user.entity.UserPositionEntity;
import teamdevhub.devhub.adapter.out.user.entity.UserSkillEntity;
import teamdevhub.devhub.adapter.out.user.mapper.UserMapper;
import teamdevhub.devhub.adapter.out.user.persistence.JpaUserPositionRepository;
import teamdevhub.devhub.adapter.out.user.persistence.JpaUserRepository;
import teamdevhub.devhub.adapter.out.user.persistence.JpaUserSkillRepository;
import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import teamdevhub.devhub.common.exception.BusinessRuleException;
import teamdevhub.devhub.domain.common.record.auth.AuthUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.user.record.UserPosition;
import teamdevhub.devhub.domain.user.record.UserSkill;
import teamdevhub.devhub.port.out.common.IdentifierProvider;
import teamdevhub.devhub.port.out.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserAdapter implements UserRepository {

    private final JpaUserRepository jpaUserRepository;
    private final JpaUserPositionRepository jpaUserPositionRepository;
    private final JpaUserSkillRepository jpaUserSkillRepository;
    private final IdentifierProvider identifierProvider;

    @Override
    public User saveNewUser(User user) {
        UserEntity saved = jpaUserRepository.save(UserMapper.toEntity(user));
        saveRelations(user);
        return UserMapper.toDomain(saved, user.getPositions(), user.getSkills());
    }

    @Override
    public AuthUser findAuthUserByEmail(String email) {
        UserEntity userEntity = jpaUserRepository.findByEmail(email).orElseThrow(() -> BusinessRuleException.of(ErrorCodeEnum.USER_NOT_FOUND));
        return UserMapper.toAuthUser(userEntity);
    }

    @Override
    public void updateLastLoginDateTime(String userGuid, LocalDateTime lastLoginDateTime) {
        jpaUserRepository.updateLastLoginDateTime(userGuid, lastLoginDateTime);
    }

    @Override
    public User findByUserGuid(String userGuid) {
        UserEntity userEntity = jpaUserRepository.findByUserGuid(userGuid).orElseThrow(() -> BusinessRuleException.of(ErrorCodeEnum.USER_NOT_FOUND));
        return UserMapper.toDomain(userEntity, loadPositions(userGuid), loadSkills(userGuid));
    }

    @Override
    public void updateUserProfile(User user) {
        jpaUserRepository.save(UserMapper.toEntity(user));
        syncPositions(user);
        syncSkills(user);
    }

    @Override
    public boolean existsByUserRole(UserRole userRole) {
        return jpaUserRepository.existsByUserRole(userRole);
    }

    private void syncPositions(User user) {
        String userGuid = user.getUserGuid();
        RelationDiff<String> diff =
                RelationDiffUtil.diff(
                        jpaUserPositionRepository.findCodesByUserGuid(userGuid),
                        extractPositionCodes(user));

        if (diff.isEmpty()) return;

        deletePositions(userGuid, diff.toDelete());
        insertPositions(userGuid, diff.toInsert());
    }

    private Set<UserPosition> loadPositions(String userGuid) {
        return jpaUserPositionRepository.findByUserGuid(userGuid).stream()
                .map(e -> new UserPosition(e.getPositionCd()))
                .collect(Collectors.toUnmodifiableSet());
    }

    private void deletePositions(String userGuid, Set<String> codes) {
        if (!codes.isEmpty()) {
            jpaUserPositionRepository.deleteByUserGuidAndPositionCdIn(userGuid, codes);
        }
    }

    private void insertPositions(String userGuid, Set<String> codes) {
        if (codes.isEmpty()) return;

        List<UserPositionEntity> entities =
                codes.stream()
                        .map(code -> UserPositionEntity.builder()
                                .userInterestPositionGuid(identifierProvider.generateIdentifier())
                                .userGuid(userGuid)
                                .positionCd(code)
                                .build())
                        .toList();

        jpaUserPositionRepository.saveAll(entities);
    }

    private void syncSkills(User user) {
        String userGuid = user.getUserGuid();

        RelationDiff<String> diff =
                RelationDiffUtil.diff(
                        jpaUserSkillRepository.findCodesByUserGuid(userGuid),
                        extractSkillCodes(user)
                );

        if (diff.isEmpty()) return;

        deleteSkills(userGuid, diff.toDelete());
        insertSkills(userGuid, diff.toInsert());
    }

    private Set<UserSkill> loadSkills(String userGuid) {
        return jpaUserSkillRepository.findByUserGuid(userGuid).stream()
                .map(e -> new UserSkill(e.getSkillCd()))
                .collect(Collectors.toUnmodifiableSet());
    }

    private void deleteSkills(String userGuid, Set<String> codes) {
        if (!codes.isEmpty()) {
            jpaUserSkillRepository.deleteByUserGuidAndSkillCdIn(userGuid, codes);
        }
    }

    private void insertSkills(String userGuid, Set<String> codes) {
        if (codes.isEmpty()) return;

        List<UserSkillEntity> entities =
                codes.stream()
                        .map(code -> UserSkillEntity.builder()
                                .userSkillGuid(identifierProvider.generateIdentifier())
                                .userGuid(userGuid)
                                .skillCd(code)
                                .build())
                        .toList();

        jpaUserSkillRepository.saveAll(entities);
    }

    private void saveRelations(User user) {
        insertPositions(user.getUserGuid(), extractPositionCodes(user));
        insertSkills(user.getUserGuid(), extractSkillCodes(user));
    }

    private Set<String> extractPositionCodes(User user) {
        return user.getPositions().stream()
                .map(UserPosition::positionCode)
                .collect(Collectors.toSet());
    }

    private Set<String> extractSkillCodes(User user) {
        return user.getSkills().stream()
                .map(UserSkill::skillCode)
                .collect(Collectors.toSet());
    }
}