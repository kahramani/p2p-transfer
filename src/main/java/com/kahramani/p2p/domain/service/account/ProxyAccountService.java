package com.kahramani.p2p.domain.service.account;

import com.kahramani.p2p.domain.entity.Account;
import com.kahramani.p2p.domain.repository.AccountJpaRepository;

import java.util.Optional;

public class ProxyAccountService implements AccountService {

    private final AccountJpaRepository accountJpaRepository;

    public ProxyAccountService(AccountJpaRepository accountJpaRepository) {
        this.accountJpaRepository = accountJpaRepository;
    }

    @Override
    public Optional<Account> retrieveAccountByReference(String reference) {
        return accountJpaRepository.findByReference(reference);
    }

    @Override
    public Optional<String> retrieveAccountCurrencyByReference(String reference) {
        return retrieveAccountByReference(reference).map(Account::getCurrency);
    }

    @Override
    public Optional<Account> retrieveAccountById(Long id) {
        return accountJpaRepository.findById(id);
    }

    @Override
    public Optional<Account> retrieveAccountByUserIdAndCurrency(Long userId, String currency) {
        return accountJpaRepository.findByUserIdAndCurrency(userId, currency);
    }

    @Override
    public void updateAccount(Account account) {
        accountJpaRepository.save(account);
    }
}