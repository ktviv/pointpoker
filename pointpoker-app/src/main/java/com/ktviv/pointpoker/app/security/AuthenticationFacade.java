package com.ktviv.pointpoker.app.security;

import com.ktviv.pointpoker.domain.entity.ApplicationUser;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface AuthenticationFacade {

    Authentication getAuthentication();

    default Optional<ApplicationUser> getCurrentAuthenticatedUser() {

        Authentication authentication = getAuthentication();
        return authentication.isAuthenticated() ? Optional.of((ApplicationUser) authentication.getPrincipal()) : Optional.empty();
    }
}
