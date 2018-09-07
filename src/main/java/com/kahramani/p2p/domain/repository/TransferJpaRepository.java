package com.kahramani.p2p.domain.repository;

import com.kahramani.p2p.domain.entity.Transfer;

import java.sql.SQLException;
import java.util.concurrent.Callable;

public interface TransferJpaRepository extends JpaRepository<Transfer, Long> {

    Transfer processInTransaction(Callable<Transfer> callable) throws SQLException;
}