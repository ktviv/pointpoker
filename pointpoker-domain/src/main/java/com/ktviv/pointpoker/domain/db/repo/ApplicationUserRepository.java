package com.ktviv.pointpoker.domain.db.repo;


import com.ktviv.pointpoker.domain.entity.ApplicationUser;

import java.util.Optional;

public interface ApplicationUserRepository {

    Optional<ApplicationUser> getApplicationUserByName(String username);

    Optional<ApplicationUser> getApplicationUserById(String userId);
}
