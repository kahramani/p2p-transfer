package com.kahramani.p2p.domain.service.user;

import com.j256.ormlite.support.ConnectionSource;
import com.kahramani.p2p.AbstractFunctionalTest;
import com.kahramani.p2p.domain.entity.User;
import com.kahramani.p2p.domain.repository.h2.H2DbUserJpaRepository;
import com.kahramani.p2p.infrastructure.config.db.session.DatabaseSessionManager;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ProxyUserServiceFT extends AbstractFunctionalTest {

    private static ProxyUserService proxyUserService;

    @BeforeClass
    public static void setUpClass() throws SQLException {
        DatabaseSessionManager<ConnectionSource> databaseSessionManager = initializeDatabaseSession("testProxyUserServiceFT");
        proxyUserService = new ProxyUserService(new H2DbUserJpaRepository(databaseSessionManager));
    }

    @Test
    public void should_return_user_for_given_mobile_number() {
        String mobileNumber = "+442033228352";
        Optional<User> optionalUser = proxyUserService.retrieveUserByMobileNumber(mobileNumber);

        assertThat(optionalUser.isPresent()).isTrue();
        User user = optionalUser.get();
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getReference()).isEqualTo("PBXDGMH02XGOGIF");
        assertThat(user.getInsertDate()).isNotNull();
        assertThat(user.getUserName()).isEqualTo("nikolay");
        assertThat(user.getMobileNumber()).isEqualTo("+442033228352");
        assertThat(user.getEmail()).isEqualTo("email@revolut.com");
    }

    @Test
    public void should_return_empty_when_mobile_number_not_exist() {
        String mobileNumber = "not-exist";
        Optional<User> optionalUser = proxyUserService.retrieveUserByMobileNumber(mobileNumber);

        assertThat(optionalUser.isPresent()).isFalse();
    }

    @Test
    public void should_return_user_for_given_user_name() {
        String userName = "taavet";
        Optional<User> optionalUser = proxyUserService.retrieveUserByUsername(userName);

        assertThat(optionalUser.isPresent()).isTrue();
        User user = optionalUser.get();
        assertThat(user.getId()).isEqualTo(2);
        assertThat(user.getReference()).isEqualTo("PVYJMUZDHY7OICK");
        assertThat(user.getInsertDate()).isNotNull();
        assertThat(user.getUserName()).isEqualTo("taavet");
        assertThat(user.getMobileNumber()).isEqualTo("+442036950999");
        assertThat(user.getEmail()).isEqualTo("email@transferwise.com");
    }

    @Test
    public void should_return_empty_when_user_name_not_exist() {
        String userName = "not-exist";
        Optional<User> optionalUser = proxyUserService.retrieveUserByUsername(userName);

        assertThat(optionalUser.isPresent()).isFalse();
    }
}