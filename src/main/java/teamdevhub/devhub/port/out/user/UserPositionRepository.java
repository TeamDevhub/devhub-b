package teamdevhub.devhub.port.out.user;

import teamdevhub.devhub.domain.user.vo.UserPosition;

import java.util.Set;

public interface UserPositionRepository {
    Set<UserPosition> findByUserGuid(String userGuid);
}
