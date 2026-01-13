package teamdevhub.devhub.fake.pure.repository;

import teamdevhub.devhub.domain.user.vo.UserSkill;
import teamdevhub.devhub.port.out.user.UserSkillRepository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FakeUserSkillRepository implements UserSkillRepository {

    private final Map<String, Set<UserSkill>> store = new HashMap<>();

    @Override
    public Set<UserSkill> findByUserGuid(String userGuid) {
        return store.getOrDefault(userGuid, Set.of());
    }

    @Override
    public void delete(Set<UserSkill> skills) {

    }


    @Override
    public void saveAll(Set<UserSkill> skills) {
        if (skills == null || skills.isEmpty()) {
            return;
        }

        String userGuid = skills.iterator().next().userGuid();

        store.put(userGuid, new HashSet<>(skills));
    }
}
