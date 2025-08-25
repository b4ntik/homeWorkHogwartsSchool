package ru.hogwarts.school.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Faculty;


import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
public class FacultyRepository implements JpaRepository<Faculty, Long> {

    @Override
    public void flush() {

    }

    @Override
    public <S extends Faculty> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Faculty> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Faculty> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Faculty getOne(Long aLong) {
        return null;
    }

    @Override
    public Faculty getById(Long aLong) {
        return null;
    }

    @Override
    public Faculty getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Faculty> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Faculty> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Faculty> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Faculty> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Faculty> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Faculty> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Faculty, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Faculty> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Faculty> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<Faculty> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<Faculty> findAll() {
        return List.of();
    }

    @Override
    public List<Faculty> findAllById(Iterable<Long> longs) {
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
    public void delete(Faculty entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Faculty> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Faculty> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Faculty> findAll(Pageable pageable) {
        return null;
    }

    @PersistenceContext
    private EntityManager entityManager;

    public Collection<Faculty> findByColor(String color) {
        String jpql = "SELECT s FROM Faculty s WHERE s.color ILIKE :color";//регистронезависимый поиск
        List<Faculty> result = entityManager.createQuery(jpql, Faculty.class)
                .setParameter("color", color)
                .getResultList();
        return result; // Возвращаем готовую коллекцию
    }
}
