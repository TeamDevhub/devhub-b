package teamdevhub.devhub.outbound.application.adapter;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.application.domain.ProjectApplication;
import teamdevhub.devhub.core.application.port.in.command.SearchAdminProjectApplicationCommand;
import teamdevhub.devhub.core.application.port.out.AdminProjectApplicationRepository;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.outbound.application.adapter.entity.ProjectApplicationEntity;
import teamdevhub.devhub.outbound.application.adapter.mapper.ApplicationMapper;
import teamdevhub.devhub.outbound.application.persistence.JpaAdminProjectApplicationRepository;
import teamdevhub.devhub.outbound.project.adapter.entity.ProjectRequirementEntity;
import teamdevhub.devhub.outbound.project.persistence.JpaProjectRequirementRepository;
import teamdevhub.devhub.outbound.user.adapter.entity.UserEntity;
import teamdevhub.devhub.outbound.user.persistence.JpaUserRepository;

@Component
@RequiredArgsConstructor
public class AdminProjectApplicationAdapter implements AdminProjectApplicationRepository {
	
	private final JpaAdminProjectApplicationRepository jpaAdminProjectApplicationRepository;
	private final JpaProjectRequirementRepository jpaProjectRequirementRepository;
	private final JpaUserRepository jpaUserRepository;
	
	@Override
	public PageResult<ProjectApplication> getApplicationsByProjectGuid(
			SearchAdminProjectApplicationCommand searchAdminProjectApplicationCommand, PageCommand pageCommand) {
		Pageable pageable = PageRequest.of(pageCommand.page(), pageCommand.size());
		Page<ProjectApplicationEntity> entityResult = jpaAdminProjectApplicationRepository.getApplicationsByProjectGuid(searchAdminProjectApplicationCommand.positionCd(),
				searchAdminProjectApplicationCommand.levelCd(), searchAdminProjectApplicationCommand.approvalStatusCd(), pageable);
		List<ProjectApplicationEntity> applicationEntities = entityResult.getContent();
		if (applicationEntities.isEmpty()) {
			return PageResult.of(
					List.of(),
					entityResult.getNumber(),
					entityResult.getSize(),
					entityResult.getTotalPages());
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

		// 모집 요건 정보 조회
		Map<String, ProjectRequirementEntity> requirementMap =
			jpaProjectRequirementRepository.findAllById(reqGuidList)
				.stream()
				.collect(Collectors.toMap(ProjectRequirementEntity::getProjectRequirementGuid, r -> r));

		List<ProjectApplication> content = applicationEntities.stream()
			.map(app -> {
				UserEntity user = userMap.get(app.getApplicantGuid());
				ProjectRequirementEntity requirement = requirementMap.get(app.getRequirementGuid());

				if (user == null || requirement == null) return null;

				return ApplicationMapper.toApplication(app, user, requirement, null);
			})
			.filter(Objects::nonNull)
			.toList();
		return PageResult.of(
				content,
				entityResult.getNumber(),
				entityResult.getSize(),
				entityResult.getTotalPages());
	}

}
