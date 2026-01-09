package teamdevhub.devhub.small.mock.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import teamdevhub.devhub.port.in.admin.command.SearchUserCommand;
import teamdevhub.devhub.adapter.out.user.entity.UserEntity;
import teamdevhub.devhub.adapter.out.user.mapper.UserMapper;
import teamdevhub.devhub.adapter.out.user.persistence.UserQueryRepository;
import teamdevhub.devhub.domain.user.User;

import java.util.*;

public class FakeUserQueryRepository implements UserQueryRepository {

    private final Map<String, UserEntity> store = new HashMap<>();

    public FakeUserQueryRepository() {
        User user1 = User.createGeneralUser(
                "GUID1",
                "user1@example.com",
                "test1234",
                "User1",
                "Intro",
                List.of("001"),
                List.of("001")
        );

        User user2 = User.createGeneralUser(
                "GUID2",
                "user2@example.com",
                "test4567",
                "User2",
                "Intro",
                List.of("001"),
                List.of("001")
        );

        store.put(user1.getUserGuid(), UserMapper.toEntity(user1));
        store.put(user2.getUserGuid(), UserMapper.toEntity(user2));
    }

    @Override
    public Page<UserEntity> listUser(SearchUserCommand searchUserCommand, Pageable pageable) {
        List<UserEntity> filtered = store.values().stream()
                .filter(user -> {
                    boolean matches = true;

                    if (searchUserCommand.getBlocked() != null) {
                        matches &= user.isBlocked() == searchUserCommand.getBlocked();
                    }
                    if (searchUserCommand.getJoinedFrom() != null) {
                        matches &= user.getRegDt() != null && !user.getRegDt().isBefore(searchUserCommand.getJoinedFrom());
                    }
                    if (searchUserCommand.getJoinedTo() != null) {
                        matches &= user.getRegDt() != null && !user.getRegDt().isAfter(searchUserCommand.getJoinedTo());
                    }
                    if (searchUserCommand.getKeyword() != null && !searchUserCommand.getKeyword().isBlank()) {
                        matches &= user.getUsername() != null &&
                                user.getUsername().toLowerCase().contains(searchUserCommand.getKeyword().toLowerCase());
                    }

                    return matches;
                })
                .sorted(Comparator.comparing(
                        UserEntity::getRegDt,
                        Comparator.nullsLast(Comparator.naturalOrder())
                ).reversed())
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filtered.size());
        List<UserEntity> pageContent;
        if (start >= end) {
            pageContent = Collections.emptyList();
        } else {
            pageContent = filtered.subList(start, end);
        }
        return new PageImpl<>(pageContent, pageable, filtered.size());
    }
}
