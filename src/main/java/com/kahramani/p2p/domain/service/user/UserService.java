package com.kahramani.p2p.domain.service.user;

import com.kahramani.p2p.domain.entity.User;

import java.util.Optional;

public interface UserService {

    Optional<User> retrieveUserByMobileNumber(String mobileNumber);

    Optional<User> retrieveUserByUsername(String username);
}