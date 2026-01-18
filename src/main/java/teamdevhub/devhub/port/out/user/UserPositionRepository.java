package teamdevhub.devhub.port.out.user;

import teamdevhub.devhub.domain.user.vo.UserPosition;

import java.util.Set;

public interface UserPositionRepository {

    void saveAll(Set<UserPosition> positions);
    Set<UserPosition> findByUserGuid(String userGuid);
    void replace(Set<UserPosition> previousPositions, Set<UserPosition> changedPositions);
}
