package teamdevhub.devhub.outbound.application.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import teamdevhub.devhub.core.application.domain.ProjectApplication;
import teamdevhub.devhub.core.application.domain.ProjectApplicationAnswer;
import teamdevhub.devhub.core.common.audit.AuditInfo;
import teamdevhub.devhub.outbound.application.adapter.entity.ProjectApplicationAnswerEntity;
import teamdevhub.devhub.outbound.application.adapter.entity.ProjectApplicationEntity;
import teamdevhub.devhub.outbound.application.adapter.mapper.ApplicationMapper;
import teamdevhub.devhub.outbound.project.adapter.entity.ProjectRequirementEntity;
import teamdevhub.devhub.outbound.user.adapter.entity.UserEntity;
import teamdevhub.devhub.outbound.user.adapter.entity.UserSkillEntity;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static teamdevhub.devhub.outbound.application.adapter.entity.QProjectApplicationAnswerEntity.projectApplicationAnswerEntity;
import static teamdevhub.devhub.outbound.application.adapter.entity.QProjectApplicationEntity.projectApplicationEntity;
import static teamdevhub.devhub.outbound.project.adapter.entity.QProjectEntity.projectEntity;
import static teamdevhub.devhub.outbound.project.adapter.entity.QProjectRequirementEntity.projectRequirementEntity;
import static teamdevhub.devhub.outbound.user.adapter.entity.QUserEntity.userEntity;
import static teamdevhub.devhub.outbound.user.adapter.entity.QUserSkillEntity.userSkillEntity;

@Repository
@RequiredArgsConstructor
public class ProjectApplicationQueryDaoImpl implements ProjectApplicationQueryDao {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<ProjectApplication> findApplicationsByProjectGuid(String projectGuid, Pageable pageable) {

		Long total = Optional.ofNullable(
			queryFactory
				.select(projectApplicationEntity.count())
				.from(projectApplicationEntity)
				.leftJoin(projectRequirementEntity)
					.on(projectApplicationEntity.requirementGuid.eq(projectRequirementEntity.projectRequirementGuid))
				.join(projectEntity)
					.on(projectRequirementEntity.projectGuid.eq(projectEntity.projectGuid)
						.and(projectEntity.projectGuid.eq(projectGuid)))
				.fetchOne()
		).orElse(0L);

		List<ProjectApplicationEntity> applicationEntities = queryFactory
			.selectFrom(projectApplicationEntity)
			.leftJoin(projectRequirementEntity)
				.on(projectApplicationEntity.requirementGuid.eq(projectRequirementEntity.projectRequirementGuid))
			.join(projectEntity)
				.on(projectRequirementEntity.projectGuid.eq(projectEntity.projectGuid)
					.and(projectEntity.projectGuid.eq(projectGuid)))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		if (applicationEntities.isEmpty()) {
			return new PageImpl<>(List.of(), pageable, total);
		}

		List<String> applicantGuidList = applicationEntities.stream()
			.map(ProjectApplicationEntity::getApplicantGuid)
			.distinct()
			.toList();

		List<String> reqGuidList = applicationEntities.stream()
			.map(ProjectApplicationEntity::getRequirementGuid)
			.distinct()
			.toList();

		Map<String, UserEntity> userMap = queryFactory
			.selectFrom(userEntity)
			.where(userEntity.userGuid.in(applicantGuidList))
			.fetch()
			.stream()
			.collect(Collectors.toMap(UserEntity::getUserGuid, u -> u));

		Map<String, List<String>> userSkillMap = queryFactory
			.selectFrom(userSkillEntity)
			.where(userSkillEntity.userGuid.in(applicantGuidList))
			.fetch()
			.stream()
			.collect(Collectors.groupingBy(
				UserSkillEntity::getUserGuid,
				Collectors.mapping(UserSkillEntity::getSkillCd, Collectors.toList())
			));

		Map<String, ProjectRequirementEntity> requirementMap = queryFactory
			.selectFrom(projectRequirementEntity)
			.where(projectRequirementEntity.projectRequirementGuid.in(reqGuidList))
			.fetch()
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

		return new PageImpl<>(content, pageable, total);
	}

	@Override
	public ProjectApplication findApplicationByGuid(String applicationGuid) {
		ProjectApplicationEntity app = queryFactory
			.selectFrom(projectApplicationEntity)
			.where(projectApplicationEntity.applicationGuid.eq(applicationGuid))
			.fetchOne();

		if (app == null) return null;

		UserEntity user = queryFactory
			.selectFrom(userEntity)
			.where(userEntity.userGuid.eq(app.getApplicantGuid()))
			.fetchOne();

		ProjectRequirementEntity requirement = queryFactory
			.selectFrom(projectRequirementEntity)
			.where(projectRequirementEntity.projectRequirementGuid.eq(app.getRequirementGuid()))
			.fetchOne();

		List<String> skillList = queryFactory
			.selectFrom(userSkillEntity)
			.where(userSkillEntity.userGuid.eq(app.getApplicantGuid()))
			.fetch()
			.stream()
			.map(UserSkillEntity::getSkillCd)
			.toList();

		if (user == null || requirement == null) return null;

		return ApplicationMapper.toApplication(app, user, requirement, skillList);
	}

	@Override
	public List<ProjectApplicationAnswer> findAnswersByApplicationGuid(String applicationGuid) {
		List<ProjectApplicationAnswerEntity> answerEntities = queryFactory
			.selectFrom(projectApplicationAnswerEntity)
			.where(projectApplicationAnswerEntity.applicationGuid.eq(applicationGuid))
			.fetch();

		if (answerEntities.isEmpty()) return List.of();

		// 지원자 정보 조회 (applicationGuid로 application 찾아서 applicantGuid 획득)
		ProjectApplicationEntity app = queryFactory
			.selectFrom(projectApplicationEntity)
			.where(projectApplicationEntity.applicationGuid.eq(applicationGuid))
			.fetchOne();

		if (app == null) return List.of();

		UserEntity user = queryFactory
			.selectFrom(userEntity)
			.where(userEntity.userGuid.eq(app.getApplicantGuid()))
			.fetchOne();

		ProjectRequirementEntity requirement = queryFactory
			.selectFrom(projectRequirementEntity)
			.where(projectRequirementEntity.projectRequirementGuid.eq(app.getRequirementGuid()))
			.fetchOne();

		List<String> skillList = queryFactory
			.selectFrom(userSkillEntity)
			.where(userSkillEntity.userGuid.eq(app.getApplicantGuid()))
			.fetch()
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
				.build()
			)
			.toList();
	}
}
