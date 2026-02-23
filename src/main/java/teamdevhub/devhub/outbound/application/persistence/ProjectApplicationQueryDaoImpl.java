package teamdevhub.devhub.outbound.application.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import teamdevhub.devhub.core.application.domain.ProjectApplication;
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

		/*
		 * SELECT *
		 * FROM PROJECT_APPLICATION T1
		 * LEFT JOIN (SELECT PROJECT_GUID, PROJECT_REQUIREMENT_GUID FROM PROJECT_REQUIREMENT) T2
		 *        ON T1.REQUIREMENT_GUID = T2.PROJECT_REQUIREMENT_GUID
		 * INNER JOIN (SELECT PROJECT_GUID FROM PROJECT) T3
		 *         ON T2.PROJECT_GUID = T3.PROJECT_GUID
		 *        AND T3.PROJECT_GUID = #{projectGuid}
		 */
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

		// 지원자 유저 정보 조회
		Map<String, UserEntity> userMap = queryFactory
			.selectFrom(userEntity)
			.where(userEntity.userGuid.in(applicantGuidList))
			.fetch()
			.stream()
			.collect(Collectors.toMap(UserEntity::getUserGuid, u -> u));

		// 지원자 스킬 목록 조회
		Map<String, List<String>> userSkillMap = queryFactory
			.selectFrom(userSkillEntity)
			.where(userSkillEntity.userGuid.in(applicantGuidList))
			.fetch()
			.stream()
			.collect(Collectors.groupingBy(
				UserSkillEntity::getUserGuid,
				Collectors.mapping(UserSkillEntity::getSkillCd, Collectors.toList())
			));

		// 모집 요건 정보 조회
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
}
