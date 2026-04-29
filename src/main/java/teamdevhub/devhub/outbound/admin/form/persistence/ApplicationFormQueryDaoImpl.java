package teamdevhub.devhub.outbound.admin.form.persistence;

import static teamdevhub.devhub.outbound.admin.form.adapter.entity.QApplicationFormEntity.applicationFormEntity;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.core.application.port.in.command.SearchApplicationFormCommand;
import teamdevhub.devhub.outbound.admin.form.adapter.entity.ApplicationFormEntity;
import teamdevhub.devhub.outbound.admin.form.adapter.mapper.ApplicationFormMapper;

@Repository
@RequiredArgsConstructor
public class ApplicationFormQueryDaoImpl implements ApplicationFormQueryDao {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<ApplicationForm> listApplicationForm(SearchApplicationFormCommand searchApplicationFormCommand,
			Pageable pageable) {

		BooleanExpression titleContains = titleCond(searchApplicationFormCommand.title());
		BooleanExpression useYnEquals = useYnCond(searchApplicationFormCommand.isUsed());
		BooleanExpression customYnEquals = customYnCond(searchApplicationFormCommand.isCustomized());

		List<ApplicationFormEntity> content = queryFactory
				.selectFrom(applicationFormEntity)
				.where(titleContains, useYnEquals, customYnEquals)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		Long total = queryFactory
				.select(applicationFormEntity.count())
				.from(applicationFormEntity)
				.where(titleContains, useYnEquals, customYnEquals)
				.fetchOne();

		List<ApplicationForm> domainList = content.stream()
				.map(ApplicationFormMapper::toApplicationForm)
				.toList();

		return new PageImpl<>(domainList, pageable, total != null ? total : 0);
	}

	@Override
	public Page<ApplicationForm> listApplicationFormWithoutItem(SearchApplicationFormCommand searchApplicationFormCommand) {

		BooleanExpression titleContains = titleCond(searchApplicationFormCommand.title());
		BooleanExpression useYnEquals = useYnCond(searchApplicationFormCommand.isUsed());
		BooleanExpression customYnEquals = customYnCond(searchApplicationFormCommand.isCustomized());

		List<ApplicationFormEntity> applicationFormEntities = queryFactory
				.selectFrom(applicationFormEntity)
				.where(titleContains, useYnEquals, customYnEquals)
				.fetch();

		List<ApplicationForm> content = applicationFormEntities.stream()
				.map(ApplicationFormMapper::toApplicationForm)
				.toList();

		return new PageImpl<>(content);
	}

	private BooleanExpression titleCond(String searchTitle) {
		if (searchTitle == null || searchTitle.isBlank()) return null;
		return applicationFormEntity.title.contains(searchTitle);
	}

	private BooleanExpression useYnCond(Boolean isUsed) {
		if (isUsed == null) return null;
		return applicationFormEntity.isUsed.eq(isUsed);
	}

	private BooleanExpression customYnCond(Boolean isCustomized) {
		if (isCustomized == null) return null;
		return applicationFormEntity.isCustomized.eq(isCustomized);
	}
}
