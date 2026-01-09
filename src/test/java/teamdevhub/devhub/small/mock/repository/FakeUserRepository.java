package teamdevhub.devhub.small.mock.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import teamdevhub.devhub.port.in.admin.command.SearchUserCommand;
import teamdevhub.devhub.adapter.in.admin.user.dto.AdminUserSummaryResponseDto;
import teamdevhub.devhub.domain.vo.auth.AuthenticatedUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.port.out.user.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class FakeUserRepository implements UserRepository {

    private final Map<String, User> store = new HashMap<>();

    @Override
    public void saveAdminUser(User adminUser) {
        store.put(adminUser.getUserGuid(), adminUser);
    }

    @Override
    public AuthenticatedUser findAuthenticatedUserByEmail(String email) {
        return store.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .map(user -> new AuthenticatedUser(user.getUserGuid(), user.getEmail(), user.getPassword(), user.getUserRole()))
                .orElse(null);
    }

    @Override
    public AuthenticatedUser findAuthenticatedUserByUserGuid(String userGuid) {
        return store.values().stream()
                .filter(user -> user.getUserGuid().equals(userGuid))
                .findFirst()
                .map(user -> new AuthenticatedUser(user.getUserGuid(), user.getEmail(), user.getPassword(), user.getUserRole()))
                .orElse(null);
    }

    @Override
    public User saveNewUser(User user) {
        store.put(user.getUserGuid(), user);
        return user;
    }

    @Override
    public void updateLastLoginDateTime(String userGuid, LocalDateTime lastLoginDateTime) {
        User existedUser = store.get(userGuid);
        if (existedUser != null) {
            User updatedUser = User.builder()
                    .userGuid(existedUser.getUserGuid())
                    .email(existedUser.getEmail())
                    .username(existedUser.getUsername())
                    .password(existedUser.getPassword())
                    .userRole(existedUser.getUserRole())
                    .introduction(existedUser.getIntroduction())
                    .positions(existedUser.getPositions())
                    .skills(existedUser.getSkills())
                    .mannerDegree(existedUser.getMannerDegree())
                    .blocked(existedUser.isBlocked())
                    .blockEndDate(existedUser.getBlockEndDate())
                    .deleted(existedUser.isDeleted())
                    .lastLoginDateTime(lastLoginDateTime)
                    .auditInfo(existedUser.getAuditInfo())
                    .build();

            store.put(userGuid, updatedUser);
        }
    }

    @Override
    public User findByUserGuid(String userGuid) {
        return store.get(userGuid);
    }

    @Override
    public void updateUserProfile(User user) {
        User existedUser = store.get(user.getUserGuid());
        if (existedUser != null) {
            existedUser.updateProfile(
                    user.getUsername(),
                    user.getIntroduction(),
                    new HashSet<>(user.getPositions()),
                    new HashSet<>(user.getSkills())
            );
        }
    }

    @Override
    public void updateUserForWithdrawal(User user) {
        store.get(user.getUserGuid());
    }

    @Override
    public boolean existsByUserRole(UserRole userRole) {
        return store.values().stream().anyMatch(user -> user.getUserRole().equals(userRole));
    }

    @Override
    public Page<AdminUserSummaryResponseDto> listUser(SearchUserCommand searchUserCommand, int page, int size) {
        List<AdminUserSummaryResponseDto> users = store.values().stream()
                .map(user -> AdminUserSummaryResponseDto.builder()
                        .userGuid(user.getUserGuid())
                        .email(user.getEmail())
                        .username(user.getUsername())
                        .introduction(user.getIntroduction())
                        .mannerDegree(user.getMannerDegree())
                        .blocked(user.isBlocked())
                        .blockEndDate(user.getBlockEndDate())
                        .deleted(user.isDeleted())
                        .lastLoginDateTime(user.getLastLoginDateTime())
                        .createdBy(user.getAuditInfo().createdBy())
                        .createdAt(user.getAuditInfo().createdAt())
                        .modifiedBy(user.getAuditInfo().modifiedBy())
                        .modifiedAt(user.getAuditInfo().modifiedAt())
                        .build())
                .collect(Collectors.toList());

        int start = page * size;
        int end = Math.min(start + size, users.size());
        List<AdminUserSummaryResponseDto> pageContent = start >= end ? Collections.emptyList() : users.subList(start, end);

        return new PageImpl<>(pageContent, org.springframework.data.domain.PageRequest.of(page, size), users.size());
    }
}
