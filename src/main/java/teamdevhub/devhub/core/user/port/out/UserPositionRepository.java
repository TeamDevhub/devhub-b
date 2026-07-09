package teamdevhub.devhub.core.user.port.out;

import teamdevhub.devhub.core.user.domain.vo.position.UserPosition;

import java.util.Set;

public interface UserPositionRepository {

    void saveAll(Set<UserPosition> positions);
    Set<UserPosition> findByUserGuid(String userGuid);
    void replace(Set<UserPosition> previousPositions, Set<UserPosition> changedPositions);
}
