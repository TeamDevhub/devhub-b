package teamdevhub.devhub.adapter.out.user;

import teamdevhub.devhub.adapter.out.user.entity.UserEntity;
import teamdevhub.devhub.domain.common.AuditInfo;
import teamdevhub.devhub.domain.user.User;

public class UserMapper {

    public static UserEntity toEntity(User user) {
        return UserEntity.builder()
                .userGuid(user.getUserGuid())
                .email(user.getEmail())
                .password(user.getPassword())
                .username(user.getUsername())
                .role(user.getUserRole())
                .build();
    }

    public static User toDomain(UserEntity userEntity) {
        return User.of(
                userEntity.getUserGuid(),
                userEntity.getEmail(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getRole(),
                toAuditInfo(userEntity)
        );
    }

    private static AuditInfo toAuditInfo(UserEntity userEntity) {
        return AuditInfo.of(
                userEntity.getCreatedBy(),
                userEntity.getCreatedAt(),
                userEntity.getModifiedBy(),
                userEntity.getModifiedAt()
        );
    }
}