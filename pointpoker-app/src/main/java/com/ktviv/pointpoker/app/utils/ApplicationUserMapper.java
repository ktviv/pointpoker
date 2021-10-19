package com.ktviv.pointpoker.app.utils;

import com.ktviv.pointpoker.domain.entity.ApplicationUser;
import com.ktviv.pointpoker.domain.entity.SessionUser;

public class ApplicationUserMapper {

    public static SessionUser fromApplicationUser(ApplicationUser applicationUser) {

        return SessionUser.of(applicationUser.getUserId(), applicationUser.getUsername(), applicationUser.getEmail());
    }
}
