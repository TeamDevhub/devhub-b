package teamdevhub.devhub.adapter.out.user.mapper;

import teamdevhub.devhub.adapter.out.user.entity.UserPositionEntity;
import teamdevhub.devhub.domain.user.vo.UserPosition;

public class UserPositionMapper {

    public static UserPositionEntity toEntity(String userPositionGuid, UserPosition userPosition) {
        return UserPositionEntity.builder()
                .userPositionGuid(userPositionGuid)
                .userGuid(userPosition.userGuid())
                .positionCd(userPosition.positionCode())
                .build();
    }

    public static UserPosition toRecord(UserPositionEntity userPositionEntity) {
        return new UserPosition(
                userPositionEntity.getUserGuid(),
                userPositionEntity.getPositionCd()
        );
    }
}
