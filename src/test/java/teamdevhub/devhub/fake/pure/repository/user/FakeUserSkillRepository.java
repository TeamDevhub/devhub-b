package teamdevhub.devhub.fake.pure.repository.user;

import teamdevhub.devhub.domain.user.vo.UserSkill;
import teamdevhub.devhub.port.out.user.UserSkillRepository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FakeUserSkillRepository implements UserSkillRepository {

    private final Map<String, Set<UserSkill>> store = new HashMap<>();
    public boolean replaceCalled = false;

    @Override
    public Set<UserSkill> findByUserGuid(String userGuid) {
        return store.getOrDefault(userGuid, Set.of());
    }

    @Override
    public void replace(Set<UserSkill> previousSkills, Set<UserSkill> currentSkills) {
        replaceCalled = true;
        if (currentSkills == null) {
            currentSkills = Set.of();
        }
        if (previousSkills == null) {
            previousSkills = Set.of();
        }

        String userGuid = !currentSkills.isEmpty()
                ? currentSkills.iterator().next().userGuid()
                : previousSkills.iterator().next().userGuid();

        Set<UserSkill> existing = store.getOrDefault(userGuid, new HashSet<>());

        Set<UserSkill> toDelete = new HashSet<>(existing);
        toDelete.removeAll(currentSkills);

        Set<UserSkill> toInsert = new HashSet<>(currentSkills);
        toInsert.removeAll(existing);

        existing.removeAll(toDelete);
        existing.addAll(toInsert);

        store.put(userGuid, existing);
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
