package teamdevhub.devhub.fake.framework.persistence.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import teamdevhub.devhub.domain.user.vo.user.UserCreateCommand;
import teamdevhub.devhub.port.in.admin.command.SearchUserCommand;
import teamdevhub.devhub.adapter.out.user.entity.UserEntity;
import teamdevhub.devhub.adapter.out.user.mapper.UserMapper;
import teamdevhub.devhub.adapter.out.infrastructure.persistence.user.UserQueryRepository;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.port.in.user.command.SignupCommand;

import java.util.*;

import static teamdevhub.devhub.constant.UserTestConstant.*;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_PASSWORD_2;

public class FakeUserQueryRepository implements UserQueryRepository {

    private final Map<String, UserEntity> store = new HashMap<>();

    public FakeUserQueryRepository() {
        SignupCommand signupCommand1 = SignupCommand.builder()
                .userGuid(null)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        UserCreateCommand generalUserCreateCommand1 = UserCreateCommand.generalUserCreateCommand(signupCommand1, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser1 = User.createGeneralUser(generalUserCreateCommand1);

        SignupCommand signupCommand2 = SignupCommand.builder()
                .userGuid(null)
                .email(TEST_EMAIL_2)
                .password(TEST_PASSWORD_2)
                .username(TEST_USERNAME_2)
                .introduction(TEST_INTRO_2)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_2)
                .build();
        UserCreateCommand generalUserCreateCommand2 = UserCreateCommand.generalUserCreateCommand(signupCommand2, TEST_USER_GUID_2, TEST_PASSWORD_2);
        User testUser2 = User.createGeneralUser(generalUserCreateCommand2);

        store.put(testUser1.getUserGuid(), UserMapper.toEntity(testUser1));
        store.put(testUser2.getUserGuid(), UserMapper.toEntity(testUser2));
    }

    @Override
    public Page<UserEntity> listUser(SearchUserCommand searchUserCommand, Pageable pageable) {
        List<UserEntity> filtered = store.values().stream()
                .filter(user -> {
                    boolean matches = true;

                    if (searchUserCommand.blocked() != null) {
                        matches &= user.isBlocked() == searchUserCommand.blocked();
                    }
                    if (searchUserCommand.joinedFrom() != null) {
                        matches &= user.getRegisteredDate() != null && !user.getRegisteredDate().isBefore(searchUserCommand.joinedFrom());
                    }
                    if (searchUserCommand.joinedTo() != null) {
                        matches &= user.getRegisteredDate() != null && !user.getRegisteredDate().isAfter(searchUserCommand.joinedTo());
                    }
                    if (searchUserCommand.keyword() != null && !searchUserCommand.keyword().isBlank()) {
                        matches &= user.getUsername() != null &&
                                user.getUsername().toLowerCase().contains(searchUserCommand.keyword().toLowerCase());
                    }

                    return matches;
                })
                .sorted(Comparator.comparing(
                        UserEntity::getRegisteredDate,
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
