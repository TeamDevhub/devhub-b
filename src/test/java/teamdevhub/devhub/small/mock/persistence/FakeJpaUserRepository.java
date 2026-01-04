package teamdevhub.devhub.small.mock.persistence;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import teamdevhub.devhub.adapter.out.user.entity.UserEntity;
import teamdevhub.devhub.adapter.out.user.persistence.JpaUserRepository;
import teamdevhub.devhub.domain.user.UserRole;

import java.util.*;
import java.util.function.Function;

public class FakeJpaUserRepository implements JpaUserRepository {

    private final Map<String, UserEntity> store = new HashMap<>();

    @Override
    public Optional<UserEntity> findByUserGuid(String userGuid) {
        return Optional.ofNullable(store.get(userGuid));
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return store.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public boolean existsByUserRole(UserRole role) {
        return store.values().stream().anyMatch(u -> u.getUserRole().equals(role));
    }

    @Override
    public <S extends UserEntity> S save(S entity) {
        store.put(entity.getUserGuid(), entity);
        return entity;
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
