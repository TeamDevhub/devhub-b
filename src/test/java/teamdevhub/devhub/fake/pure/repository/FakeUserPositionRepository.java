package teamdevhub.devhub.fake.pure.repository;

import teamdevhub.devhub.domain.user.vo.UserPosition;
import teamdevhub.devhub.port.out.user.UserPositionRepository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FakeUserPositionRepository implements UserPositionRepository {

    private final Map<String, Set<UserPosition>> store = new HashMap<>();

    @Override
    public Set<UserPosition> findByUserGuid(String userGuid) {
        return store.getOrDefault(userGuid, Set.of());
    }

    @Override
    public void saveAll(Set<UserPosition> positions) {
        if (positions == null || positions.isEmpty()) {
            return;
        }

        String userGuid = positions.iterator().next().userGuid();

        store.put(userGuid, new HashSet<>(positions));
    }
}
