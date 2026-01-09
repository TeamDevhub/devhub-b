package teamdevhub.devhub.small.mock.persistence.mail;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import teamdevhub.devhub.adapter.out.mail.entity.EmailVerificationEntity;
import teamdevhub.devhub.adapter.out.mail.persistence.JpaEmailVerificationRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class FakeJpaEmailVerificationRepository implements JpaEmailVerificationRepository {

    private final Map<String, EmailVerificationEntity> store = new HashMap<>();

    @Override
    public Optional<EmailVerificationEntity> findById(String email) {
        return Optional.ofNullable(store.get(email));
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public <S extends EmailVerificationEntity> S save(S entity) {
        store.put(entity.getEmail(), entity);
        return entity;
    }

    @Override
    public void deleteById(String email) {
        store.remove(email);
    }

    @Override
    public void delete(EmailVerificationEntity entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends EmailVerificationEntity> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends EmailVerificationEntity> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends EmailVerificationEntity> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<EmailVerificationEntity> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<String> strings) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public EmailVerificationEntity getOne(String s) {
        return null;
    }

    @Override
    public EmailVerificationEntity getById(String s) {
        return null;
    }

    @Override
    public EmailVerificationEntity getReferenceById(String s) {
        return null;
    }

    @Override
    public <S extends EmailVerificationEntity> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends EmailVerificationEntity> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends EmailVerificationEntity> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends EmailVerificationEntity> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends EmailVerificationEntity> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends EmailVerificationEntity> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends EmailVerificationEntity, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends EmailVerificationEntity> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public List<EmailVerificationEntity> findAll() {
        return List.of();
    }

    @Override
    public List<EmailVerificationEntity> findAllById(Iterable<String> strings) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public List<EmailVerificationEntity> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<EmailVerificationEntity> findAll(Pageable pageable) {
        return null;
    }
}
