package teamdevhub.devhub.outbound.admin.form.persistence;

import static teamdevhub.devhub.outbound.admin.form.adapter.entity.QApplicationFormEntity.applicationFormEntity;
import static teamdevhub.devhub.outbound.admin.form.adapter.entity.QApplicationFormItemEntity.applicationFormItemEntity;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.core.application.port.in.command.SearchApplicationFormCommand;
import teamdevhub.devhub.outbound.admin.form.adapter.entity.ApplicationFormEntity;
import teamdevhub.devhub.outbound.admin.form.adapter.entity.ApplicationFormItemEntity;
import teamdevhub.devhub.outbound.admin.form.adapter.mapper.ApplicationFormMapper;
@Repository
@RequiredArgsConstructor
public class ApplicationFormQueryDaoImpl implements ApplicationFormQueryDao{
	
	private final JPAQueryFactory queryFactory;
	
	@Override
	public Page<ApplicationForm> listApplicationForm(SearchApplicationFormCommand searchApplicationFormCommand,
			Pageable pageable) {
		
		BooleanExpression titleContains = titleCond(searchApplicationFormCommand.title());
		BooleanExpression useYnEqauls = useYnCond(searchApplicationFormCommand.isUsed());
		BooleanExpression customYnEqauls = customYnCond(searchApplicationFormCommand.isCustomized());
		
		JPAQuery<?> commonQuery = queryFactory
				.select(applicationFormEntity)
				.from(applicationFormEntity)
				.where(titleContains, useYnEqauls, customYnEqauls);
		
		List<String> applicationFormGuidList = commonQuery
				.select(applicationFormEntity.applicationFormGuid)
				.distinct()
				.fetch();
		
		List<ApplicationFormEntity> applicationForms = queryFactory
				.select(applicationFormEntity)
				.from(applicationFormEntity)
				.where(applicationFormEntity.applicationFormGuid.in(applicationFormGuidList))
				.fetch();
		
		List<ApplicationFormItemEntity> applicationFormItems = queryFactory
				.select(applicationFormItemEntity)
				.from(applicationFormItemEntity)
				.where(applicationFormItemEntity.formGuid.in(applicationFormGuidList))
				.fetch();
		
		return null;
	}

	private BooleanExpression titleCond(String searchTitle) {
		if(searchTitle == null || searchTitle.isBlank()) return null;
		return applicationFormEntity.title.contains(searchTitle);
	}
	
	private BooleanExpression useYnCond(Boolean isUsed) {
		if(isUsed == null) return null;
		return applicationFormEntity.isUsed.eq(isUsed);
	}
	
	private BooleanExpression customYnCond(Boolean isCustomized) {
		if(isCustomized == null) return null;
		return applicationFormEntity.isCustomized.eq(isCustomized);
	}

	@Override
	public Page<ApplicationForm> listApplicationFormWithoutItem(SearchApplicationFormCommand searchApplicationFormCommand) {
		
		BooleanExpression titleContains = titleCond(searchApplicationFormCommand.title());
		BooleanExpression useYnEqauls = useYnCond(searchApplicationFormCommand.isUsed());
		BooleanExpression customYnEqauls = customYnCond(searchApplicationFormCommand.isCustomized());
		
		List<ApplicationFormEntity> applicationFormEntitys = queryFactory
				.select(applicationFormEntity)
				.from(applicationFormEntity)
				.where(titleContains, useYnEqauls, customYnEqauls)
				.fetch();
		
		List<ApplicationForm> content = applicationFormEntitys.stream().map(entity -> {
			return ApplicationFormMapper.toApplicationForm(entity);
		}).toList();
		
		return new PageImpl<>(content);
	}

}
