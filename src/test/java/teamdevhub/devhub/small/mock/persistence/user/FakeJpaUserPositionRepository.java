package teamdevhub.devhub.small.mock.persistence.user;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import teamdevhub.devhub.adapter.out.user.entity.UserPositionEntity;
import teamdevhub.devhub.adapter.out.user.persistence.JpaUserPositionRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FakeJpaUserPositionRepository implements JpaUserPositionRepository {

    private final Map<String, Set<UserPositionEntity>> store = new HashMap<>();

    @Override
    public List<UserPositionEntity> findByUserGuid(String userGuid) {
        return new ArrayList<>(store.getOrDefault(userGuid, Collections.emptySet()));
    }

    @Override
    public Set<String> findCodesByUserGuid(String userGuid) {
        return store.getOrDefault(userGuid, Collections.emptySet())
                .stream()
                .map(UserPositionEntity::getPositionCd)
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteByUserGuidAndPositionCdIn(String userGuid, Set<String> toDelete) {
        Set<UserPositionEntity> positions = store.getOrDefault(userGuid, new HashSet<>());
        positions.removeIf(p -> toDelete.contains(p.getPositionCd()));
        store.put(userGuid, positions);
    }

    @Override
    public <S extends UserPositionEntity> S save(S entity) {
        return null;
    }

    @Override
    public <S extends UserPositionEntity> List<S> saveAll(Iterable<S> entities) {
        for (S entity : entities) {
            store.computeIfAbsent(entity.getUserGuid(), k -> new HashSet<>()).add(entity);
        }
        List<S> list = new ArrayList<>();
        entities.forEach(list::add);
        return list;
    }

    @Override
    public Optional<UserPositionEntity> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<UserPositionEntity> findAll() {
        return List.of();
    }

    @Override
    public List<UserPositionEntity> findAllById(Iterable<String> strings) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(UserPositionEntity entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends UserPositionEntity> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends UserPositionEntity> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends UserPositionEntity> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<UserPositionEntity> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<String> strings) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public UserPositionEntity getOne(String s) {
        return null;
    }

    @Override
    public UserPositionEntity getById(String s) {
        return null;
    }

    @Override
    public UserPositionEntity getReferenceById(String s) {
        return null;
    }

    @Override
    public <S extends UserPositionEntity> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends UserPositionEntity> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends UserPositionEntity> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends UserPositionEntity> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends UserPositionEntity> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends UserPositionEntity> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends UserPositionEntity, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public List<UserPositionEntity> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<UserPositionEntity> findAll(Pageable pageable) {
        return null;
    }
}
