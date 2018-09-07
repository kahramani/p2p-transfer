package com.kahramani.p2p.domain.repository;

import com.kahramani.p2p.domain.entity.Account;

import java.util.Optional;

public interface AccountJpaRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUserIdAndCurrency(Long userId, String currency);
}