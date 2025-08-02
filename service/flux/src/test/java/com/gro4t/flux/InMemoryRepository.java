package com.gro4t.flux;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.FluentQuery;

public class InMemoryRepository<T> implements MongoRepository<T, String> {
  protected ConcurrentHashMap<String, T> map = new ConcurrentHashMap<>();

  @Override
  public <S extends T> S insert(S entity) {
    return null;
  }

  @Override
  public <S extends T> List<S> insert(Iterable<S> entities) {
    return List.of();
  }

  @Override
  public <S extends T> Optional<S> findOne(Example<S> example) {
    return Optional.empty();
  }

  @Override
  public <S extends T> List<S> findAll(Example<S> example) {
    return List.of();
  }

  @Override
  public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
    return List.of();
  }

  @Override
  public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
    return null;
  }

  @Override
  public <S extends T> long count(Example<S> example) {
    return 0;
  }

  @Override
  public <S extends T> boolean exists(Example<S> example) {
    return false;
  }

  @Override
  public <S extends T, R> R findBy(
      Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
    return null;
  }

  @Override
  public <S extends T> S save(S entity) {
    return null;
  }

  @Override
  public <S extends T> List<S> saveAll(Iterable<S> entities) {
    return List.of();
  }

  @Override
  public Optional<T> findById(String s) {
    return Optional.empty();
  }

  @Override
  public boolean existsById(String s) {
    return false;
  }

  @Override
  public List<T> findAll() {
    return List.of();
  }

  @Override
  public List<T> findAllById(Iterable<String> strings) {
    return List.of();
  }

  @Override
  public long count() {
    return 0;
  }

  @Override
  public void deleteById(String s) {}

  @Override
  public void delete(T entity) {}

  @Override
  public void deleteAllById(Iterable<? extends String> strings) {}

  @Override
  public void deleteAll(Iterable<? extends T> entities) {}

  @Override
  public void deleteAll() {}

  @Override
  public List<T> findAll(Sort sort) {
    return List.of();
  }

  @Override
  public Page<T> findAll(Pageable pageable) {
    return null;
  }
}
