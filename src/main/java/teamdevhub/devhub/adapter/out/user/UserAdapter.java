package teamdevhub.devhub.adapter.out.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.adapter.in.common.vo.PageResult;
import teamdevhub.devhub.adapter.out.exception.AdapterDataException;
import teamdevhub.devhub.adapter.out.user.entity.UserEntity;
import teamdevhub.devhub.adapter.out.user.mapper.UserMapper;
import teamdevhub.devhub.adapter.out.infrastructure.persistence.user.JpaUserRepository;
import teamdevhub.devhub.adapter.out.infrastructure.persistence.user.UserQueryRepository;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.port.in.admin.command.SearchUserCommand;
import teamdevhub.devhub.port.out.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserAdapter implements UserRepository {

    private final JpaUserRepository jpaUserRepository;
    private final UserQueryRepository userQueryRepository;

    @Override
    public void saveAdminUser(User adminUser) {
        jpaUserRepository.save(UserMapper.toEntity(adminUser));
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = jpaUserRepository.save(UserMapper.toEntity(user));
        return UserMapper.toDomain(userEntity);
    }

    @Override
    public AuthenticatedUser findAuthenticatedUserByEmail(String email) {
        UserEntity userEntity = jpaUserRepository.findByEmail(email).
                orElseThrow(() -> AdapterDataException.of(ErrorCode.USER_NOT_FOUND));
        return UserMapper.toAuthenticatedUser(userEntity);
    }

    @Override
    public AuthenticatedUser findAuthenticatedUserByUserGuid(String userGuid) {
        UserEntity userEntity = jpaUserRepository.findByUserGuid(userGuid).
                orElseThrow(() -> AdapterDataException.of(ErrorCode.USER_NOT_FOUND));
        return UserMapper.toAuthenticatedUser(userEntity);
    }

    @Override
    public User findByUserGuid(String userGuid) {
        UserEntity userEntity = jpaUserRepository.findByUserGuid(userGuid)
                .orElseThrow(() -> AdapterDataException.of(ErrorCode.USER_NOT_FOUND));
        return UserMapper.toDomain(userEntity);
    }

    @Override
    public Optional<User> findOptionalByEmail(String email) {
        return jpaUserRepository.findByEmail(email).map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByOAuth(VerificationProvider verificationProvider, String oauthId) {
        return jpaUserRepository.findByProviderAndOauthId(verificationProvider, oauthId).map(UserMapper::toDomain);
    }

    @Override
    public void updateLastLoginDateTime(String userGuid, LocalDateTime lastLoginDateTime) {
        jpaUserRepository.updateLastLoginDateTime(userGuid, lastLoginDateTime);
    }

    @Override
    public void updateUserProfile(User user) {
        jpaUserRepository.save(UserMapper.toEntity(user));
    }

    @Override
    public void delete(User user) {
        jpaUserRepository.save(UserMapper.toEntity(user));
    }

    @Override
    public boolean existsByUserRole(UserRole userRole) {
        return jpaUserRepository.existsByUserRole(userRole);
    }

    @Override
    public PageResult<User> listUser(SearchUserCommand searchUserCommand, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("registeredDate").descending());
        Page<UserEntity> pagedUserEntityList = userQueryRepository.listUser(searchUserCommand, pageable);

        List<User> userList = pagedUserEntityList.getContent().stream()
                .map(UserMapper::toDomain)
                .toList();

        return PageResult.of(
                userList,
                pagedUserEntityList.getNumber(),
                pagedUserEntityList.getSize(),
                pagedUserEntityList.getTotalElements());
    }
}