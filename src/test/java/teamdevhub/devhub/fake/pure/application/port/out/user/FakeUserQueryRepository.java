package teamdevhub.devhub.fake.pure.application.port.out.user;

import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.port.in.command.SearchUserCommand;
import teamdevhub.devhub.core.user.port.out.UserQueryRepository;

import java.util.*;

public class FakeUserQueryRepository implements UserQueryRepository {

    private final Map<String, User> store = new HashMap<>();

    public User save(User user) {
        store.put(user.getUserGuid(), user);
        return user;
    }

    @Override
    public PageResult<User> listUser(SearchUserCommand searchUserCommand, int page, int size) {
        List<User> userList = new ArrayList<>(store.values());

        long totalElements = userList.size();
        int start = page * size;
        int end = Math.min(start + size, userList.size());
        List<User> pageContent = start >= end ? Collections.emptyList() : userList.subList(start, end);

        return PageResult.of(
                pageContent,
                page,
                size,
                totalElements
        );
    }
}
