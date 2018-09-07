package com.kahramani.p2p.domain.repository;

import java.util.Optional;

public interface JpaRepository<T, ID> {

    T save(T t);

    Optional<T> findById(ID id);

    Optional<T> findByReference(String reference);
}