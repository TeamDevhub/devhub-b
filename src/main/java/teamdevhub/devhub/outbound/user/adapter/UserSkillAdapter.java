package teamdevhub.devhub.outbound.user.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.outbound.user.adapter.entity.UserSkillEntity;
import teamdevhub.devhub.outbound.user.adapter.mapper.UserSkillMapper;
import teamdevhub.devhub.outbound.user.persistence.JpaUserSkillRepository;
import teamdevhub.devhub.core.user.domain.vo.skill.UserSkill;
import teamdevhub.devhub.core.user.port.out.UserSkillRepository;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.shared.util.RelationChangeUtil;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
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
    public void replace(Set<UserSkill> previousSkills, Set<UserSkill> changedSkills) {
        if (changedSkills == null || changedSkills.isEmpty()) {
            return;
        }

        syncSkills(previousSkills, changedSkills);
    }

    private void syncSkills(Set<UserSkill> previousSkills, Set<UserSkill> currentSkills) {
        String userGuid = currentSkills.iterator().next().userGuid();

        Set<String> oldSkillCds = previousSkills.stream()
                .map(UserSkill::skillCd)
                .collect(Collectors.toSet());

        Set<String> newSkillCds = currentSkills.stream()
                .map(UserSkill::skillCd)
                .collect(Collectors.toSet());

        RelationChangeUtil.RelationChange<String> change = RelationChangeUtil.change(oldSkillCds, newSkillCds);

        if (change.isEmpty()) {
            return;
        }

        deleteSkills(userGuid, change.toDelete());
        insertSkills(userGuid, change.toInsert());
    }

    private void deleteSkills(String userGuid, Set<String> skillCds) {
        if (!skillCds.isEmpty()) {
            jpaUserSkillRepository.deleteByUserGuidAndSkillCdIn(userGuid, skillCds);
        }
    }

    private void insertSkills(String userGuid, Set<String> skillCds) {
        if (skillCds.isEmpty()) {
            return;
        }

        List<UserSkillEntity> userSkillEntityList = skillCds.stream()
                .map(skillCd -> UserSkillEntity.builder()
                        .userSkillGuid(identifierProvider.generateIdentifier())
                        .userGuid(userGuid)
                        .skillCd(skillCd)
                        .build())
                .toList();

        jpaUserSkillRepository.saveAll(userSkillEntityList);
    }
}
