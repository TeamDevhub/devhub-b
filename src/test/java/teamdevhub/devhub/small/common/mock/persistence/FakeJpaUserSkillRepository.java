package teamdevhub.devhub.small.common.mock.persistence;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import teamdevhub.devhub.adapter.out.user.entity.UserSkillEntity;
import teamdevhub.devhub.adapter.out.user.persistence.JpaUserSkillRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FakeJpaUserSkillRepository implements JpaUserSkillRepository {

    private final Map<String, Set<UserSkillEntity>> store = new HashMap<>();

    @Override
    public List<UserSkillEntity> findByUserGuid(String userGuid) {
        return new ArrayList<>(store.getOrDefault(userGuid, Collections.emptySet()));
    }

    @Override
    public Set<String> findCodesByUserGuid(String userGuid) {
        return store.getOrDefault(userGuid, Collections.emptySet())
                .stream()
                .map(UserSkillEntity::getSkillCd)
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteByUserGuidAndSkillCdIn(String userGuid, Set<String> toDelete) {
        Set<UserSkillEntity> skills = store.getOrDefault(userGuid, new HashSet<>());
        skills.removeIf(s -> toDelete.contains(s.getSkillCd()));
        store.put(userGuid, skills);
    }

    @Override
    public <S extends UserSkillEntity> S save(S entity) {
        return null;
    }

    @Override
    public <S extends UserSkillEntity> List<S> saveAll(Iterable<S> entities) {
        for (S entity : entities) {
            store.computeIfAbsent(entity.getUserGuid(), k -> new HashSet<>()).add(entity);
        }
        List<S> list = new ArrayList<>();
        entities.forEach(list::add);
        return list;
    }

    @Override
    public Optional<UserSkillEntity> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<UserSkillEntity> findAll() {
        return List.of();
    }

    @Override
    public List<UserSkillEntity> findAllById(Iterable<String> strings) {
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
    public void delete(UserSkillEntity entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends UserSkillEntity> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends UserSkillEntity> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends UserSkillEntity> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<UserSkillEntity> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<String> strings) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public UserSkillEntity getOne(String s) {
        return null;
    }

    @Override
    public UserSkillEntity getById(String s) {
        return null;
    }

    @Override
    public UserSkillEntity getReferenceById(String s) {
        return null;
    }

    @Override
    public <S extends UserSkillEntity> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends UserSkillEntity> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends UserSkillEntity> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends UserSkillEntity> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends UserSkillEntity> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends UserSkillEntity> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends UserSkillEntity, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public List<UserSkillEntity> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<UserSkillEntity> findAll(Pageable pageable) {
        return null;
    }
}
