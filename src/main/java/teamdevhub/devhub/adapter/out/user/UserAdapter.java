package teamdevhub.devhub.adapter.out.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.port.in.admin.command.SearchUserCommand;
import teamdevhub.devhub.adapter.in.admin.user.dto.AdminUserSummaryResponseDto;
import teamdevhub.devhub.adapter.out.exception.AdapterDataException;
import teamdevhub.devhub.common.util.RelationChangeUtil;
import teamdevhub.devhub.adapter.out.user.entity.UserEntity;
import teamdevhub.devhub.adapter.out.user.entity.UserPositionEntity;
import teamdevhub.devhub.adapter.out.user.entity.UserSkillEntity;
import teamdevhub.devhub.adapter.out.user.mapper.UserMapper;
import teamdevhub.devhub.adapter.out.user.persistence.JpaUserPositionRepository;
import teamdevhub.devhub.adapter.out.user.persistence.JpaUserRepository;
import teamdevhub.devhub.adapter.out.user.persistence.JpaUserSkillRepository;
import teamdevhub.devhub.adapter.out.user.persistence.UserQueryRepository;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.domain.vo.auth.AuthenticatedUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.user.vo.UserPosition;
import teamdevhub.devhub.domain.user.vo.UserSkill;
import teamdevhub.devhub.common.provider.uuid.IdentifierProvider;
import teamdevhub.devhub.port.out.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserAdapter implements UserRepository {

    private final JpaUserRepository jpaUserRepository;
    private final UserQueryRepository userQueryRepository;
    private final JpaUserPositionRepository jpaUserPositionRepository;
    private final JpaUserSkillRepository jpaUserSkillRepository;
    private final IdentifierProvider identifierProvider;

    @Override
    public void saveAdminUser(User adminUser) {
        jpaUserRepository.save(UserMapper.toEntity(adminUser));
    }

    @Override
    public AuthenticatedUser findAuthenticatedUserByEmail(String email) {
        UserEntity userEntity = jpaUserRepository.findByEmail(email).
                orElseThrow(() -> AdapterDataException.of(ErrorCode.USER_NOT_FOUND));
        return UserMapper.toAuthenticatedUser(userEntity);
    }

    @Override
    public AuthenticatedUser findAuthenticatedUserByUserGuid(String userGuid) {
        UserEntity userEntity = jpaUserRepository.findByUserGuid(userGuid).
                orElseThrow(() -> AdapterDataException.of(ErrorCode.USER_NOT_FOUND));
        return UserMapper.toAuthenticatedUser(userEntity);
    }

    @Override
    public User saveNewUser(User user) {
        UserEntity userEntity = jpaUserRepository.save(UserMapper.toEntity(user));
        insertPositions(user.getUserGuid(), extractPositionCodes(user));
        insertSkills(user.getUserGuid(), extractSkillCodes(user));
        return UserMapper.toDomain(userEntity, user.getPositions(), user.getSkills());
    }

    @Override
    public void updateLastLoginDateTime(String userGuid, LocalDateTime lastLoginDateTime) {
        if(jpaUserRepository.updateLastLoginDateTime(userGuid, lastLoginDateTime) == 0) {
            throw AdapterDataException.of(ErrorCode.UPDATE_FAIL);
        }
    }

    @Override
    public User findByUserGuid(String userGuid) {
        UserEntity userEntity = jpaUserRepository.findByUserGuid(userGuid)
                .orElseThrow(() -> AdapterDataException.of(ErrorCode.USER_NOT_FOUND));
        return UserMapper.toDomain(userEntity, loadPositions(userGuid), loadSkills(userGuid));
    }

    @Override
    public void updateUserProfile(User user) {
        jpaUserRepository.save(UserMapper.toEntity(user));
        syncPositions(user);
        syncSkills(user);
    }

    @Override
    public void updateUserForWithdrawal(User user) {
        jpaUserRepository.save(UserMapper.toEntity(user));
    }

    @Override
    public boolean existsByUserRole(UserRole userRole) {
        return jpaUserRepository.existsByUserRole(userRole);
    }

    @Override
    public Page<AdminUserSummaryResponseDto> listUser(SearchUserCommand searchUserCommand, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("regDt").descending());
        Page<UserEntity> pagedUserEntityList = userQueryRepository.listUser(searchUserCommand, pageable);

        List<AdminUserSummaryResponseDto> userList = pagedUserEntityList.getContent().stream()
                .map(AdminUserSummaryResponseDto::fromEntity)
                .toList();
        return new PageImpl<>(userList, pageable, pagedUserEntityList.getTotalElements());
    }

    private void syncPositions(User user) {
        String userGuid = user.getUserGuid();
        RelationChangeUtil.RelationChange<String> relationChange = RelationChangeUtil.change(
                jpaUserPositionRepository.findCodesByUserGuid(userGuid),
                extractPositionCodes(user));

        if (relationChange.isEmpty()) {
            return;
        }

        deletePositions(userGuid, relationChange.toDelete());
        insertPositions(userGuid, relationChange.toInsert());
    }

    private Set<UserPosition> loadPositions(String userGuid) {
        return jpaUserPositionRepository.findByUserGuid(userGuid).stream()
                .map(userPositionEntity -> new UserPosition(userPositionEntity.getPositionCd()))
                .collect(Collectors.toUnmodifiableSet());
    }

    private void deletePositions(String userGuid, Set<String> positionCodeSet) {
        if (!positionCodeSet.isEmpty()) {
            jpaUserPositionRepository.deleteByUserGuidAndPositionCdIn(userGuid, positionCodeSet);
        }
    }

    private void insertPositions(String userGuid, Set<String> positionCodeSet) {
        if (positionCodeSet.isEmpty()) {
            return;
        }

        List<UserPositionEntity> userPositionEntityList = positionCodeSet.stream()
                .map(positionCode -> UserPositionEntity.builder()
                        .userInterestPositionGuid(identifierProvider.generateIdentifier())
                        .userGuid(userGuid)
                        .positionCd(positionCode)
                        .build())
                .toList();
        jpaUserPositionRepository.saveAll(userPositionEntityList);
    }

    private void syncSkills(User user) {
        String userGuid = user.getUserGuid();

        RelationChangeUtil.RelationChange<String> relationChange = RelationChangeUtil.change(
                jpaUserPositionRepository.findCodesByUserGuid(userGuid),
                extractSkillCodes(user));

        if (relationChange.isEmpty()) {
            return;
        }

        deleteSkills(userGuid, relationChange.toDelete());
        insertSkills(userGuid, relationChange.toInsert());
    }

    private Set<UserSkill> loadSkills(String userGuid) {
        return jpaUserSkillRepository.findByUserGuid(userGuid).stream()
                .map(userSkillEntity -> new UserSkill(userSkillEntity.getSkillCd()))
                .collect(Collectors.toUnmodifiableSet());
    }

    private void deleteSkills(String userGuid, Set<String> skillCodeSet) {
        if (!skillCodeSet.isEmpty()) {
            jpaUserSkillRepository.deleteByUserGuidAndSkillCdIn(userGuid, skillCodeSet);
        }
    }

    private void insertSkills(String userGuid, Set<String> skillCodeSet) {
        if (skillCodeSet.isEmpty()) {
            return;
        }

        List<UserSkillEntity> userSkillEntityList = skillCodeSet.stream()
                .map(skillCode -> UserSkillEntity.builder()
                        .userSkillGuid(identifierProvider.generateIdentifier())
                        .userGuid(userGuid)
                        .skillCd(skillCode)
                        .build())
                .toList();
        jpaUserSkillRepository.saveAll(userSkillEntityList);
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