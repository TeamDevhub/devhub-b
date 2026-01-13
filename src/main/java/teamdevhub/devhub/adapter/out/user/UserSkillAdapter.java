package teamdevhub.devhub.adapter.out.user;

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
    public Set<UserSkill> findByUserGuid(String userGuid) {
        return jpaUserSkillRepository.findByUserGuid(userGuid)
                .stream()
                .map(UserSkillMapper::toRecord)
                .collect(Collectors.toSet());
    }

    @Override
    public void delete(Set<UserSkill> skills) {
        if (skills == null || skills.isEmpty()) {
            return;
        }

        skills.forEach(skill ->
                jpaUserSkillRepository.deleteByUserGuidAndSkillCd(
                        skill.userGuid(),
                        skill.skillCode()
                )
        );
    }
}
