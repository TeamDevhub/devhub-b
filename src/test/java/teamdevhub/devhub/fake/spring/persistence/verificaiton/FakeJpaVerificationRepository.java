package teamdevhub.devhub.fake.spring.persistence.verificaiton;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import teamdevhub.devhub.adapter.out.infrastructure.persistence.verification.JpaVerificationRepository;
import teamdevhub.devhub.adapter.out.verification.entity.VerificationEntity;
import teamdevhub.devhub.domain.verification.vo.VerificationType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class FakeJpaVerificationRepository implements JpaVerificationRepository {

    private final Map<String, VerificationEntity> store = new HashMap<>();

    @Override
    public Optional<VerificationEntity> findByVerificationTypeAndTargetValue(VerificationType verificationType, String value) {
        return Optional.ofNullable(store.get(key(verificationType, value)));
    }

    @Override
    public VerificationEntity save(VerificationEntity verificationEntity) {
        store.put(key(verificationEntity.getVerificationType(), verificationEntity.getTargetValue()), verificationEntity);
        return verificationEntity;
    }

    @Override
    public void deleteByVerificationTypeAndTargetValue(VerificationType verificationType, String value) {
        store.remove(key(verificationType, value));
    }

    private String key(VerificationType verificationType, String value) {
        return verificationType + ":" + value;
    }

    public boolean exists(VerificationType verificationType, String value) {
        return store.containsKey(key(verificationType, value));
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends VerificationEntity> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends VerificationEntity> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<VerificationEntity> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public VerificationEntity getOne(Long aLong) {
        return null;
    }

    @Override
    public VerificationEntity getById(Long aLong) {
        return null;
    }

    @Override
    public VerificationEntity getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends VerificationEntity> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends VerificationEntity> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends VerificationEntity> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends VerificationEntity> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends VerificationEntity> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends VerificationEntity> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends VerificationEntity, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends VerificationEntity> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<VerificationEntity> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<VerificationEntity> findAll() {
        return List.of();
    }

    @Override
    public List<VerificationEntity> findAllById(Iterable<Long> longs) {
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
    public void delete(VerificationEntity entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends VerificationEntity> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<VerificationEntity> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<VerificationEntity> findAll(Pageable pageable) {
        return null;
    }
}
