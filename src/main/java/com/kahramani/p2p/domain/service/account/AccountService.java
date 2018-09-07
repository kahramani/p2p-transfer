package com.kahramani.p2p.domain.service.account;

import com.kahramani.p2p.domain.entity.Account;

import java.util.Optional;

public interface AccountService {

    Optional<Account> retrieveAccountByReference(String reference);

    Optional<String> retrieveAccountCurrencyByReference(String reference);

    Optional<Account> retrieveAccountById(Long id);

    Optional<Account> retrieveAccountByUserIdAndCurrency(Long userId, String currency);

    void updateAccount(Account account);
}