package teamdevhub.devhub.fake.pure.repository;

import teamdevhub.devhub.domain.user.vo.UserPosition;
import teamdevhub.devhub.port.out.user.UserPositionRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FakeUserPositionRepository implements UserPositionRepository {

    private final Map<String, Set<String>> store = new HashMap<>();

    @Override
    public Set<UserPosition> findByUserGuid(String userGuid) {
        return store.getOrDefault(userGuid, Set.of())
                .stream()
                .map(UserPosition::new)
                .collect(Collectors.toUnmodifiableSet());
    }
}
