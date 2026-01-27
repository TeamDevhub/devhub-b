package teamdevhub.devhub.infrastructure.user.adapter.out.mapper;

import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.infrastructure.user.adapter.out.entity.UserEntity;
import teamdevhub.devhub.core.common.audit.AuditInfo;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;

public class UserMapper {

    public static AuthenticatedUser toAuthenticatedUser(UserEntity userEntity) {
        return AuthenticatedUser.of(
                userEntity.getUserGuid(),
                userEntity.getEmail(),
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
                .username(user.getUsername())
                .userRole(user.getUserRole())
                .introduction(user.getIntroduction())
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
                userEntity.getUsername(),
                userEntity.getUserRole(),
                userEntity.getIntroduction(),
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