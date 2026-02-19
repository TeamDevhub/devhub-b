package teamdevhub.devhub.outbound.user.adapter.mapper;

import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.outbound.user.adapter.entity.UserEntity;
import teamdevhub.devhub.core.common.audit.AuditInfo;
import teamdevhub.devhub.outbound.auth.infrastructure.security.vo.AuthenticatedUser;

public class UserMapper {

    public static AuthenticatedUser toAuthenticatedUser(UserEntity userEntity) {
        return AuthenticatedUser.of(
                userEntity.getUserGuid(),
                userEntity.getEmail(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getUserRole()
        );
    }

    public static UserEntity toEntity(User user) {
        return UserEntity.builder()
                .userGuid(user.getUserGuid())
                .provider(user.getVerificationProvider())
                .oauthId(user.getOauthId())
                .email(user.getEmail())
                .password(user.getPassword())
                .userRole(user.getUserRole())
                .username(user.getUsername())
                .introduction(user.getIntroduction())
                .fileGuid(user.getFileGuid())
                .mannerDegree(user.getMannerDegree())
                .blocked(user.isBlocked())
                .blockEndDate(user.getBlockEndDate())
                .deleted(user.isDeleted())
                .lastLoginDate(user.getLastLoginDate())
                .build();
    }

    public static User toDomain(UserEntity userEntity) {
        return User.of(
                userEntity.getUserGuid(),
                userEntity.getProvider(),
                userEntity.getOauthId(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getUserRole(),
                userEntity.getUsername(),
                userEntity.getIntroduction(),
                userEntity.getFileGuid(),
                userEntity.getMannerDegree(),
                userEntity.isBlocked(),
                userEntity.getBlockEndDate(),
                userEntity.isDeleted(),
                userEntity.getLastLoginDate(),
                toAuditInfo(userEntity)
        );
    }

    private static AuditInfo toAuditInfo(UserEntity userEntity) {
        return AuditInfo.of(
                userEntity.getRegistrantGuid(),
                userEntity.getRegisteredDate(),
                userEntity.getModifierGuid(),
                userEntity.getModifiedDate()
        );
    }
}