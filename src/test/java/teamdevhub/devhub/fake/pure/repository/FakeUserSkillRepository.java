package teamdevhub.devhub.fake.pure.repository;

import teamdevhub.devhub.domain.user.vo.UserSkill;
import teamdevhub.devhub.port.out.user.UserSkillRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FakeUserSkillRepository implements UserSkillRepository {

    private final Map<String, Set<String>> store = new HashMap<>();

    @Override
    public Set<UserSkill> findByUserGuid(String userGuid) {
        return store.getOrDefault(userGuid, Set.of())
                .stream()
                .map(UserSkill::new)
                .collect(Collectors.toUnmodifiableSet());
    }
}
