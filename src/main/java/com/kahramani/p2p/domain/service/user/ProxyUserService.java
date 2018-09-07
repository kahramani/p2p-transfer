package com.kahramani.p2p.domain.service.user;

import com.kahramani.p2p.domain.entity.User;
import com.kahramani.p2p.domain.repository.UserJpaRepository;

import java.util.Optional;

public class ProxyUserService implements UserService {

    private final UserJpaRepository userJpaRepository;

    public ProxyUserService(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Optional<User> retrieveUserByMobileNumber(String mobileNumber) {
        return userJpaRepository.findByMobileNumber(mobileNumber);
    }

    @Override
    public Optional<User> retrieveUserByUsername(String username) {
        return userJpaRepository.findByUsername(username);
    }
}