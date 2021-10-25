package com.ktviv.pointpoker.infra.db.repo;

import com.google.common.collect.Lists;
import com.ktviv.pointpoker.domain.db.repo.ApplicationUserRepository;
import com.ktviv.pointpoker.domain.entity.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ktviv.pointpoker.domain.entity.ApplicationUserRole.ADMIN;
import static com.ktviv.pointpoker.domain.entity.ApplicationUserRole.PARTICIPANT;

@Repository("fake")
public class FakeApplicationUserDAOServiceImpl implements ApplicationUserRepository {

    private final PasswordEncoder passwordEncoder;
    private final List<ApplicationUser> appUsers;

    @Autowired
    public FakeApplicationUserDAOServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.appUsers = Lists.newArrayList(
                new ApplicationUser(
                        UUID.randomUUID().toString(),
                        "vivek",
                        passwordEncoder.encode("vivek@123"),
                        "vivek@gmail.com",
                        PARTICIPANT.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true
                ),
                new ApplicationUser(
                        UUID.randomUUID().toString(),
                        "mark",
                        passwordEncoder.encode("mark@123"),
                        "mark@gmail.com",
                        PARTICIPANT.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true
                ),
                new ApplicationUser(
                        UUID.randomUUID().toString(),
                        "mina",
                        passwordEncoder.encode("mina@123"),
                        "mina@gmail.com",
                        PARTICIPANT.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true
                ),
                new ApplicationUser(
                        UUID.randomUUID().toString(),
                        "edith",
                        passwordEncoder.encode("edith@123"),
                        "edith@gmail.com",
                        PARTICIPANT.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true
                ),
                new ApplicationUser(
                        UUID.randomUUID().toString(),
                        "test_user",
                        passwordEncoder.encode("test_user@123"),
                        "test_user@gmail.com",
                        ADMIN.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true
                ),
                new ApplicationUser(
                        UUID.randomUUID().toString(),
                        "test_user2",
                        passwordEncoder.encode("test_user2@123"),
                        "test_user2@gmail.com",
                        ADMIN.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true
                )
        );
    }

    @Override
    public Optional<ApplicationUser> getApplicationUserByName(String username) {

        return appUsers
                .stream()
                .filter(user -> username.equals(user.getUsername()))
                .findFirst();
    }

    @Override
    public Optional<ApplicationUser> getApplicationUserById(String userId) {

        return appUsers
                .stream()
                .filter(user -> userId.equals(user.getUserId()))
                .findFirst();
    }

}
