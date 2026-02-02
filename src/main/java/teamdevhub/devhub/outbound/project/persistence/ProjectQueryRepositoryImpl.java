package teamdevhub.devhub.outbound.project.persistence;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static teamdevhub.devhub.outbound.project.adapter.entity.QProjectEntity.projectEntity;
import static teamdevhub.devhub.outbound.project.adapter.entity.QProjectLikeEntity.projectLikeEntity;
import static teamdevhub.devhub.outbound.project.adapter.entity.QProjectRequirementEntity.projectRequirementEntity;
import static teamdevhub.devhub.outbound.project.adapter.entity.QProjectSkillEntity.projectSkillEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.project.domain.ProjectDetail;
import teamdevhub.devhub.core.project.port.in.command.SearchProjectListCommand;
import teamdevhub.devhub.outbound.project.adapter.entity.ProjectEntity;
import teamdevhub.devhub.outbound.project.adapter.entity.ProjectRequirementEntity;
import teamdevhub.devhub.outbound.project.adapter.mapper.ProjectMapper;

@Repository
@RequiredArgsConstructor
public class ProjectQueryRepositoryImpl implements ProjectQueryRepository {

    private final JPAQueryFactory queryFactory;
    
    public record ProjectDetailFlatDto(
	    ProjectEntity projectEntity,
	    List<String> skillCds,
	    List<ProjectRequirementEntity> requirementEntities,
	    String likeCount
	) {}

	@Override
	public Page<ProjectDetail> listProject(SearchProjectListCommand searchProjectListCommand, Pageable pageable) {
		
		BooleanExpression[] projectCond = projectIn(searchProjectListCommand);
	    BooleanExpression[] skillCond = skillIn(searchProjectListCommand);
	    BooleanExpression[] positionCond = positionIn(searchProjectListCommand);

	    // 2. 배열들 합치기
	    BooleanExpression[] allConditions = combine(projectCond, skillCond, positionCond);
	    		
		JPAQuery<?> commonQuery = queryFactory
				.from(projectEntity)
                .leftJoin(projectSkillEntity).on(projectEntity.projectGuid.eq(projectSkillEntity.projectGuid))
                .leftJoin(projectRequirementEntity).on(projectEntity.projectGuid.eq(projectRequirementEntity.projectGuid))
                .where(allConditions);
		
		List<ProjectDetail> content = commonQuery
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .transform(
                	groupBy(projectEntity.projectGuid).list(
	        			Projections.constructor(ProjectDetailFlatDto.class,
	        					projectEntity,
	        	                list(projectSkillEntity.skillCd),
	        	                list(projectRequirementEntity),
	                            JPAExpressions
		                            .select(projectLikeEntity.count().stringValue())
		                            .from(projectLikeEntity)
		                            .where(projectLikeEntity.projectGuid.eq(projectEntity.projectGuid))
	                    )
                    )
                )
                .stream()
                .map(ProjectMapper::toProjectDetail)
                .toList()
                ;
		
        Long total = Optional.ofNullable(commonQuery
        		.select(projectEntity.projectGuid.countDistinct())
        	    .fetchOne()
	    ).orElse(0L);        
		
		return new PageImpl<>(content, pageable, total);
	}
	
	private BooleanExpression[] projectIn(SearchProjectListCommand searchProjectListCommand) {
		return new BooleanExpression[] {
		        conditionIn(projectEntity.recuritmentTypeCd, searchProjectListCommand.projectRecruitTypeList()),
		    };
	}

	private BooleanExpression[] skillIn(SearchProjectListCommand searchProjectListCommand) {
	    return new BooleanExpression[] {
		        conditionIn(projectSkillEntity.skillCd, searchProjectListCommand.skillCodeList()),
		    };
	}

	private BooleanExpression[] positionIn(SearchProjectListCommand searchProjectListCommand) {
	    return new BooleanExpression[] {
		        conditionIn(projectRequirementEntity.positionCd, searchProjectListCommand.positionCodeList()),
		    };
	}
	
	private BooleanExpression[] combine(BooleanExpression[]... arrays) {
	    return Arrays.stream(arrays)
	            .flatMap(Arrays::stream)
	            .filter(Objects::nonNull)
	            .toArray(BooleanExpression[]::new);
	}
	
	private BooleanExpression conditionIn(StringPath path, List<String> values) {
	    return (values == null || values.isEmpty()) ? null : path.in(values);
	}

}