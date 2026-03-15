package teamdevhub.devhub.outbound.application.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import teamdevhub.devhub.core.application.domain.ProjectApplication;
import teamdevhub.devhub.core.application.domain.ProjectApplicationAnswer;
import teamdevhub.devhub.outbound.application.adapter.entity.ProjectApplicationAnswerEntity;
import teamdevhub.devhub.outbound.application.adapter.entity.ProjectApplicationEntity;
import teamdevhub.devhub.outbound.application.adapter.mapper.ApplicationMapper;
import teamdevhub.devhub.outbound.project.adapter.entity.ProjectRequirementEntity;
import teamdevhub.devhub.outbound.project.persistence.JpaProjectRequirementRepository;
import teamdevhub.devhub.outbound.user.adapter.entity.UserEntity;
import teamdevhub.devhub.outbound.user.adapter.entity.UserSkillEntity;
import teamdevhub.devhub.outbound.user.persistence.JpaUserRepository;
import teamdevhub.devhub.outbound.user.persistence.JpaUserSkillRepository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProjectApplicationQueryDaoImpl implements ProjectApplicationQueryDao {

	private final JpaProjectApplicationRepository jpaProjectApplicationRepository;
	private final JpaProjectApplicationAnswerRepository jpaProjectApplicationAnswerRepository;
	private final JpaProjectRequirementRepository jpaProjectRequirementRepository;
	private final JpaUserRepository jpaUserRepository;
	private final JpaUserSkillRepository jpaUserSkillRepository;

	@Override
	public Page<ProjectApplication> findApplicationsByProjectGuid(String projectGuid, Pageable pageable) {

		// projectGuid → requirementGuid 목록 조회
		List<String> requirementGuidList = jpaProjectRequirementRepository
			.findByProjectGuid(projectGuid)
			.stream()
			.map(ProjectRequirementEntity::getProjectRequirementGuid)
			.toList();

		if (requirementGuidList.isEmpty()) {
			return new PageImpl<>(List.of(), pageable, 0);
		}

		// requirementGuid 목록으로 application 페이징 조회
		Page<ProjectApplicationEntity> applicationPage =
			jpaProjectApplicationRepository.findByRequirementGuidIn(requirementGuidList, pageable);

		List<ProjectApplicationEntity> applicationEntities = applicationPage.getContent();

		if (applicationEntities.isEmpty()) {
			return new PageImpl<>(List.of(), pageable, applicationPage.getTotalElements());
		}

		List<String> applicantGuidList = applicationEntities.stream()
			.map(ProjectApplicationEntity::getApplicantGuid)
			.distinct()
			.toList();

		List<String> reqGuidList = applicationEntities.stream()
			.map(ProjectApplicationEntity::getRequirementGuid)
			.distinct()
			.toList();

		// 지원자 유저 정보 조회
		Map<String, UserEntity> userMap = jpaUserRepository.findAllById(applicantGuidList)
			.stream()
			.collect(Collectors.toMap(UserEntity::getUserGuid, u -> u));

		// 지원자 스킬 목록 조회
		Map<String, List<String>> userSkillMap = jpaUserSkillRepository.findByUserGuidIn(applicantGuidList)
			.stream()
			.collect(Collectors.groupingBy(
				UserSkillEntity::getUserGuid,
				Collectors.mapping(UserSkillEntity::getSkillCd, Collectors.toList())
			));

		// 모집 요건 정보 조회
		Map<String, ProjectRequirementEntity> requirementMap =
			jpaProjectRequirementRepository.findAllById(reqGuidList)
				.stream()
				.collect(Collectors.toMap(ProjectRequirementEntity::getProjectRequirementGuid, r -> r));

		List<ProjectApplication> content = applicationEntities.stream()
			.map(app -> {
				UserEntity user = userMap.get(app.getApplicantGuid());
				ProjectRequirementEntity requirement = requirementMap.get(app.getRequirementGuid());
				List<String> skillList = userSkillMap.getOrDefault(app.getApplicantGuid(), List.of());

				if (user == null || requirement == null) return null;

				return ApplicationMapper.toApplication(app, user, requirement, skillList);
			})
			.filter(Objects::nonNull)
			.toList();

		return new PageImpl<>(content, pageable, applicationPage.getTotalElements());
	}

	@Override
	public ProjectApplication findApplicationByGuid(String applicationGuid) {
		ProjectApplicationEntity app = jpaProjectApplicationRepository
			.findByApplicationGuid(applicationGuid)
			.orElse(null);

		if (app == null) return null;

		UserEntity user = jpaUserRepository.findByUserGuid(app.getApplicantGuid()).orElse(null);

		ProjectRequirementEntity requirement = jpaProjectRequirementRepository
			.findByProjectRequirementGuid(app.getRequirementGuid())
			.orElse(null);

		List<String> skillList = jpaUserSkillRepository.findByUserGuid(app.getApplicantGuid())
			.stream()
			.map(UserSkillEntity::getSkillCd)
			.toList();

		if (user == null || requirement == null) return null;

		return ApplicationMapper.toApplication(app, user, requirement, skillList);
	}

	@Override
	public List<ProjectApplicationAnswer> findAnswersByApplicationGuid(String applicationGuid) {
		List<ProjectApplicationAnswerEntity> answerEntities =
			jpaProjectApplicationAnswerRepository.findByApplicationGuid(applicationGuid);

		if (answerEntities.isEmpty()) return List.of();

		ProjectApplicationEntity app = jpaProjectApplicationRepository
			.findByApplicationGuid(applicationGuid)
			.orElse(null);

		if (app == null) return List.of();

		UserEntity user = jpaUserRepository.findByUserGuid(app.getApplicantGuid()).orElse(null);

		ProjectRequirementEntity requirement = jpaProjectRequirementRepository
			.findByProjectRequirementGuid(app.getRequirementGuid())
			.orElse(null);

		List<String> skillList = jpaUserSkillRepository.findByUserGuid(app.getApplicantGuid())
			.stream()
			.map(UserSkillEntity::getSkillCd)
			.toList();

		return answerEntities.stream()
			.map(answer -> ProjectApplicationAnswer.builder()
				.applicationAnswerGuid(answer.getApplicationAnswerGuid())
				.applicationGuid(answer.getApplicationGuid())
				.projectApplicationFormGuid(answer.getProjectApplicationFormGuid())
				.fileGuid(answer.getFileGuid())
				.content(answer.getContent())
				.nickName(user != null ? user.getUsername() : null)
				.email(user != null ? user.getEmail() : null)
				.mannerDegree(user != null ? user.getMannerDegree() : 0)
				.userSkillList(skillList)
				.positionCd(requirement != null ? requirement.getPositionCd() : null)
				.introduction(user != null ? user.getIntroduction() : null)
				.aplyDate(app.getRegisteredDate() != null
					? app.getRegisteredDate().toLocalDate().toString() : null)
				.build()
			)
			.toList();
	}
}
