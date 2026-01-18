package teamdevhub.devhub.adapter.out.infrastructure.persistence.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import teamdevhub.devhub.port.in.admin.command.SearchUserCommand;
import teamdevhub.devhub.adapter.out.user.entity.QUserEntity;
import teamdevhub.devhub.adapter.out.user.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserQueryRepositoryImpl implements UserQueryRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<UserEntity> listUser(SearchUserCommand searchUserCommand, Pageable pageable) {
        QUserEntity userEntity = QUserEntity.userEntity;

        List<UserEntity> content = queryFactory
                .selectFrom(userEntity)
                .where(
                        blockedCondition(searchUserCommand.blocked()),
                        joinedFromCondition(searchUserCommand.joinedFrom()),
                        joinedToCondition(searchUserCommand.joinedTo()),
                        keywordCondition(searchUserCommand.keyword()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(userEntity.registeredDate.desc())
                .fetch();

        Long total = queryFactory
                .select(userEntity.count())
                .from(userEntity)
                .where(
                        blockedCondition(searchUserCommand.blocked()),
                        joinedFromCondition(searchUserCommand.joinedFrom()),
                        joinedToCondition(searchUserCommand.joinedTo()),
                        keywordCondition(searchUserCommand.keyword()))
                .fetchOne();

        if (total == null) {
            total = 0L;
        }

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression blockedCondition(Boolean blocked) {
        if (blocked == null) {
            return null;
        }
        return QUserEntity.userEntity.blocked.eq(blocked);
    }

    private BooleanExpression joinedFromCondition(LocalDateTime joinedFrom) {
        if (joinedFrom == null) {
            return null;
        }
        return QUserEntity.userEntity.registeredDate.goe(joinedFrom);
    }

    private BooleanExpression joinedToCondition(LocalDateTime joinedTo) {
        if (joinedTo == null) {
            return null;
        }
        return QUserEntity.userEntity.registeredDate.loe(joinedTo);
    }

    private BooleanExpression keywordCondition(String keyword) {
        if (keyword == null) {
            return null;
        }
        return QUserEntity.userEntity.username.containsIgnoreCase(keyword);
    }
}