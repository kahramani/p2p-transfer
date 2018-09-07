package com.kahramani.p2p.domain.repository;

import com.kahramani.p2p.domain.entity.User;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    Optional<User> findByMobileNumber(String mobileNumber);

    Optional<User> findByUsername(String username);
}