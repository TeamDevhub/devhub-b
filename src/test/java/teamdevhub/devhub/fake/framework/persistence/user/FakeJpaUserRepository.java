package teamdevhub.devhub.fake.framework.persistence.user;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import teamdevhub.devhub.infrastructure.user.adapter.out.entity.UserEntity;
import teamdevhub.devhub.infrastructure.user.adapter.out.persistence.JpaUserRepository;
import teamdevhub.devhub.shared.enums.VerificationProvider;
import teamdevhub.devhub.core.user.domain.vo.UserRole;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

public class FakeJpaUserRepository implements JpaUserRepository {

    private final Map<String, UserEntity> store = new HashMap<>();

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return Optional.ofNullable(store.get(email));
    }

    @Override
    public Optional<UserEntity> findByUserGuid(String userGuid) {
        return Optional.ofNullable(store.get(userGuid));
    }

    @Override
    public Optional<UserEntity> findByProviderAndOauthId(VerificationProvider verificationProvider, String oauthId) {
        return Optional.empty();
    }

    @Override
    public int updateLastLoginDateTime(String userGuid, LocalDateTime now) {
        return 0;
    }

    @Override
    public boolean existsByUserRole(UserRole userRole) {
        return store.values().stream().anyMatch(u -> u.getUserRole().equals(userRole));
    }

    @Override
    public <S extends UserEntity> S save(S entity) {
        store.put(entity.getUserGuid(), entity);
        return entity;
    }

    public <S extends UserEntity> void saveForSignup(S entity) {
        store.put(entity.getEmail(), entity);
    }

    @Override
    public void deleteById(String s) { store.remove(s); }
    @Override
    public void delete(UserEntity entity) { store.remove(entity.getUserGuid()); }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends UserEntity> entities) {

    }

    @Override
    public <S extends UserEntity> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<UserEntity> findById(String s) {
        return Optional.empty();
    }

    @Override
    public List<UserEntity> findAll() { return new ArrayList<>(store.values()); }
    @Override
    public List<UserEntity> findAllById(Iterable<String> strings) { return null; }
    @Override
    public long count() { return store.size(); }
    @Override
    public void deleteAll() { store.clear(); }
    @Override
    public boolean existsById(String s) { return store.containsKey(s); }

    @Override
    public void flush() {
    }

    @Override
    public <S extends UserEntity> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends UserEntity> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<UserEntity> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<String> strings) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public UserEntity getOne(String s) {
        return null;
    }

    @Override
    public UserEntity getById(String s) {
        return null;
    }

    @Override
    public UserEntity getReferenceById(String s) {
        return null;
    }

    @Override
    public <S extends UserEntity> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends UserEntity> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends UserEntity> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends UserEntity> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends UserEntity> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends UserEntity> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends UserEntity, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public List<UserEntity> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<UserEntity> findAll(Pageable pageable) {
        return null;
    }
}
