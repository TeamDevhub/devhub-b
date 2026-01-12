package teamdevhub.devhub.adapter.out.user;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import teamdevhub.devhub.adapter.out.user.entity.UserSkillEntity;
import teamdevhub.devhub.adapter.out.user.mapper.UserSkillMapper;
import teamdevhub.devhub.adapter.out.user.persistence.JpaUserSkillRepository;
import teamdevhub.devhub.common.provider.uuid.IdentifierProvider;
import teamdevhub.devhub.domain.user.vo.UserSkill;
import teamdevhub.devhub.port.out.user.UserSkillRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserSkillAdapter implements UserSkillRepository {

    private final JpaUserSkillRepository jpaUserSkillRepository;
    private final IdentifierProvider identifierProvider;
    private final EntityManager entityManager;

    @Override
    public Set<UserSkill> findByUserGuid(String userGuid) {
        return jpaUserSkillRepository.findByUserGuid(userGuid)
                .stream()
                .map(UserSkillMapper::toRecord)
                .collect(Collectors.toSet());
    }

    @Override
    public void saveAll(Set<UserSkill> skills) {
        if (skills.isEmpty()) {
            return;
        }

        List<UserSkillEntity> userSkillEntityList = skills.stream()
                .map(userSkill -> {
                    String userSkillGuid = identifierProvider.generateIdentifier();
                    return UserSkillMapper.toEntity(userSkillGuid, userSkill);
                })
                .toList();

        jpaUserSkillRepository.saveAll(userSkillEntityList);
    }

    @Override
    public void replaceAll(Set<UserSkill> skills) {
        if (skills.isEmpty()) {
            return;
        }

        String userGuid = skills.iterator().next().userGuid();

        jpaUserSkillRepository.deleteByUserGuid(userGuid);
        entityManager.flush();
        entityManager.clear();

        List<UserSkillEntity> userSkillEntityList = skills.stream()
                .map(userSkill -> UserSkillEntity.builder()
                        .userSkillGuid(identifierProvider.generateIdentifier())
                        .userGuid(userSkill.userGuid())
                        .skillCd(userSkill.skillCode())
                        .build())
                .toList();

        jpaUserSkillRepository.saveAll(userSkillEntityList);
    }
}
