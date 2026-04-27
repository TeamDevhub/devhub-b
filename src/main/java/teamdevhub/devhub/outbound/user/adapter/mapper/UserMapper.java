package teamdevhub.devhub.outbound.user.adapter.mapper;

import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.outbound.user.adapter.entity.UserEntity;
import teamdevhub.devhub.core.common.audit.AuditInfo;

public class UserMapper {

    public static UserEntity toEntity(User user) {
        return UserEntity.builder()
                .userGuid(user.getUserGuid())
                .userRole(user.getUserRole())
                .username(user.getUsername())
                .introduction(user.getIntroduction())
                .fileGuid(user.getFileGuid())
                .mannerDegree(user.getMannerDegree())
                .blocked(user.isBlocked())
                .blockEndDate(user.getBlockEndDate())
                .deleted(user.isDeleted())
                .build();
    }

    public static User toDomain(UserEntity userEntity) {
        return User.of(
                userEntity.getUserGuid(),
                userEntity.getUserRole(),
                userEntity.getUsername(),
                userEntity.getIntroduction(),
                userEntity.getFileGuid(),
                userEntity.getMannerDegree(),
                userEntity.isBlocked(),
                userEntity.getBlockEndDate(),
                userEntity.isDeleted(),
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
