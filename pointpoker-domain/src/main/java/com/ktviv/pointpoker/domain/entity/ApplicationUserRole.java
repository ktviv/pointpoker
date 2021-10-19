package com.ktviv.pointpoker.domain.entity;

import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.ktviv.pointpoker.domain.entity.ApplicationUserPermission.POLL_CREATE;
import static com.ktviv.pointpoker.domain.entity.ApplicationUserPermission.POLL_JOIN;
import static com.ktviv.pointpoker.domain.entity.ApplicationUserPermission.POLL_RESET;
import static com.ktviv.pointpoker.domain.entity.ApplicationUserPermission.POLL_VOTE;

public enum ApplicationUserRole {


    PARTICIPANT(Sets.newHashSet(POLL_CREATE, POLL_JOIN, POLL_VOTE, POLL_RESET)),
    ADMIN(Sets.newHashSet(POLL_CREATE, POLL_JOIN, POLL_VOTE, POLL_RESET));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getSimpleGrantedAuthorities() {

        Set<SimpleGrantedAuthority> permissions = getPermissions().stream().map(permission -> new SimpleGrantedAuthority(permission.getPermission())).collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }

    public Set<GrantedAuthority> getGrantedAuthorities() {

        Set<GrantedAuthority> permissions = getPermissions().stream().map(permission -> new SimpleGrantedAuthority(permission.getPermission())).collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
