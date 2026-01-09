package teamdevhub.devhub.small.mock.persistence.auth;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import teamdevhub.devhub.adapter.out.auth.entity.RefreshTokenEntity;
import teamdevhub.devhub.adapter.out.auth.persistence.JpaRefreshTokenRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class FakeJpaRefreshTokenRepository implements JpaRefreshTokenRepository {

    private final Map<String, RefreshTokenEntity> store = new HashMap<>();
    private long sequence = 1L;

    public Optional<RefreshTokenEntity> findByUserGuid(String userGuid) {
        return Optional.ofNullable(store.get(userGuid));
    }

    public RefreshTokenEntity save(RefreshTokenEntity entity) {
        if (entity.getId() == null) {
            entity = RefreshTokenEntity.builder()
                    .id(sequence++)
                    .userGuid(entity.getUserGuid())
                    .token(entity.getToken())
                    .build();
        }

        store.put(entity.getUserGuid(), entity);
        return entity;
    }

    public void deleteByUserGuid(String userGuid) {
        store.remove(userGuid);
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends RefreshTokenEntity> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends RefreshTokenEntity> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<RefreshTokenEntity> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public RefreshTokenEntity getOne(Long aLong) {
        return null;
    }

    @Override
    public RefreshTokenEntity getById(Long aLong) {
        return null;
    }

    @Override
    public RefreshTokenEntity getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends RefreshTokenEntity> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends RefreshTokenEntity> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends RefreshTokenEntity> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends RefreshTokenEntity> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends RefreshTokenEntity> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends RefreshTokenEntity> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends RefreshTokenEntity, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends RefreshTokenEntity> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<RefreshTokenEntity> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<RefreshTokenEntity> findAll() {
        return List.of();
    }

    @Override
    public List<RefreshTokenEntity> findAllById(Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(RefreshTokenEntity entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends RefreshTokenEntity> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<RefreshTokenEntity> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<RefreshTokenEntity> findAll(Pageable pageable) {
        return null;
    }
}
